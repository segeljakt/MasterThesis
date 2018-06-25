object Rust {
  import sys.process._
  import java.io.File

  import scala.annotation.StaticAnnotation
  import scala.reflect.macros.blackbox.Context
  import language.experimental.macros
  import scala.language.postfixOps

  class setting(value: String) extends StaticAnnotation

  class RustException(msg: String) extends Exception(msg)

  def runSnippet(snippet: String, nodetype: String): (Int, StringBuilder) = {
    val errorMsg = new StringBuilder
    val errorCode = nodetype match {
      case "full" => {
        val tmp = new File("tmp.rs")
        val e = s"echo $snippet" #>
          tmp #&&
          "rustc tmp.rs --crate-type=lib" !
          ProcessLogger(x => errorMsg.append(x+"\n"))
        tmp.delete()
        e
      }
      case _ =>
        s"echo $snippet" #|
          s"./rust-parser $nodetype" !
          ProcessLogger(x => errorMsg.append(x+"\n"))
    }
    (errorCode, errorMsg)
  }


  implicit class Interpolator(val sc: StringContext) {
    @setting("full") def crust (args: String*): String = macro rustcImpl
    @setting("file") def crustf(args: String*): String = macro rustcImpl
    @setting("expr") def cruste(args: String*): String = macro rustcImpl
    @setting("item") def crusti(args: String*): String = macro rustcImpl
    @setting("type") def crustt(args: String*): String = macro rustcImpl
    def rrust (args: String*): String = rustrImpl(sc, "full")(args)
    def rrustf(args: String*): String = rustrImpl(sc, "file")(args)
    def rruste(args: String*): String = rustrImpl(sc, "expr")(args)
    def rrusti(args: String*): String = rustrImpl(sc, "item")(args)
    def rrustt(args: String*): String = rustrImpl(sc, "type")(args)
  }

  def getSnippet(strings: Seq[String], splices: Seq[String]) = {
    val stringsiter = strings.iterator
    val splicesiter = splices.iterator
    var snippetbuf = new StringBuffer(stringsiter.next)
    while(stringsiter.hasNext) {
      snippetbuf append splicesiter.next
      snippetbuf append stringsiter.next
    }
    snippetbuf.toString
  }

  def rustrImpl(sc: StringContext, nodetype: String)(args: Seq[String]): String = {
    val snippet = getSnippet(sc.parts, args)
    val (errorCode, errorMsg) = runSnippet(snippet, nodetype)
    if(errorCode != 0) {
      throw new RustException(s"$errorMsg\n>> $snippet <<\n")
    }
    snippet
  }
  // https://stackoverflow.com/questions/20805160/scala-macro-get-value-for-term-name
  def getSplice(c: Context)(a: c.Expr[String]): String = {
    import c.universe._
    val (enclosing, name) = a.tree match {
      case Select(This(enclosing), name) => enclosing -> name
      case _ => c.abort(c.enclosingPosition, "Not a `this` memver")
    }
    val impl = c.enclosingClass match {
      case impl: ImplDef if impl.name.toString == enclosing.toString => impl
      case impl: ImplDef => c.abort(c.enclosingPosition, "Should search in another parent")
      case _ => c.abort(c.enclosingPosition, "Not an `ImplDef`")
    }
    val body = impl.children.collect {
      case Template(_, _, body) => body
    } match {
      case Seq(body) => body
      case _ => c.abort(c.enclosingPosition, "Should be a single template.")
    }
    val rhss = body.collect{
      case ValDef(_, valName, _, rhs) if valName.toString == name.toString => rhs
    }
    val rhs = rhss match {
      case Seq(rhs) => rhs
      case Seq() => c.abort(c.enclosingPosition, "Not found. Maybe it's a DefDef or somethong else")
      case _ => c.abort(c.enclosingPosition, "Some other error.")
    }
    rhs match {
      case Literal(Constant(x)) => x.toString
      case _ => c.abort(c.enclosingPosition, s"Expected literal, found $rhs.")
    }
  }
  def getStrings(c: Context): Seq[String] = {
    import c.universe._
    val exprs = c.prefix.tree match {
      case Apply(_, List(Apply(_, x))) => x
      case _ => c.abort(c.enclosingPosition, "Error: Malformed quasiquote")
    }
    exprs map {
      case Literal(Constant(snippet: String)) => snippet
      case _ => c.abort(c.enclosingPosition, "Error: Malformed quasiquote")
    }
  }
  def getNodetype(c: Context): String = {
    import c.universe._
    c.macroApplication.symbol.annotations.find(
      _.tree.tpe <:< typeOf[setting]
    ).flatMap(
      _.tree.children.tail.collectFirst {
        case Literal(Constant(s: String)) => s
      }
    ).getOrElse(
      c.abort(c.enclosingPosition, "Error: No nodetype specified")
    )
  }

  def rustcImpl(c: Context)(args: c.Expr[String]*): c.Expr[String] = {
    import c.universe._
    val splices = args.map(x => getSplice(c)(x))
    val strings = getStrings(c)
    val nodetype = getNodetype(c)
    val snippet = getSnippet(strings,splices)
    val (errorCode, errorMsg) = runSnippet(snippet, nodetype)
    if(errorCode != 0) {
      c.abort(c.enclosingPosition, s"${errorMsg.toString}\n>> $snippet <<\n")
    }
    c.Expr[String](Literal(Constant(snippet)))
  }
}
