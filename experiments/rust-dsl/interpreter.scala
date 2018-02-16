
//import scala.language.experimental.macros
//import scala.reflect.macros.whitebox.Context

object Loops {

  //def Loop(cond: => Boolean)(body: => Unit): List[Int] = macro LoopImpl

  //def LoopImpl(c: Context)(f: c.Tree) = {
    //import c.universe._
    //q"""{
      //implicit var sequence
    //}"""
  //}


  import scala.collection.mutable.ListBuffer


  //def While(condition: => Boolean)(body: => Unit): List[Int] = {
    //var seq: List[Int] = List()
    //body
    //seq
  //}

  def Let(value: Int)(implicit seq: ListBuffer[Int]): Int = {
    seq += value
  }
  //def Loop(body: => Unit): Unit = {
    //while(true) {
      //body
    //}
  //}
  //def If(condition: => Boolean)(body1: => Unit)(body2: => Unit): Unit =
    //if (condition) {
      //body1
    //} else {
      //body2
    //}

  
  //def ElseImpl(c: blackbox.Context): c.Expr[Unit] = {
    //import c.universe._
    //c.Expr(q"""println("hello!")""")
  //}
  //def Else: Unit = macro ElseImpl

  def main(args: Array[String]): Unit = {
      implicit var seq: ListBuffer[Int] = new ListBuffer[Int]()
      val v1 = Let(3)
      val v2 = Let(4)
      println(seq)
    //}
    //println(x)
    //If(i == 0) {
      //println("0000000");
    //} Else {
      //println("---");
    //}
  }
}


//class x {
  //def While(condition: TExp[Boolean])(body: Unit): RustAST = {

    //var seq = List()

    //body

    //Statement(condition, seq)
  //}

  //def aasd = {
    

    //var x = 0;
    //While(x < 3) {


      //val l1 = new Exp[Int](3);
      //val l2 = new Exp[Int](3);
      //val l3 = new Add(l1.label, l2.label);


    //}









  //}
//}







//trait Codec[T] {
  //def write(x: T): Unit
//}

//object asd {
//implicit def intCodec: Codec[Int] = ???

//implicit def optionCodec[T] (implicit ev: => Codec[T]): Codec[Option[T]] =
  //new {
    //def write(xo: Option[T]) = xo match {
      //case Some(x) => ev.write(x)
      //case None =>
    //}
  //}


//def main(args: Array[String]): Unit = {
  //val s = implicitly[Codec[Option[Int]]]

  //s.write(Some(33))
  //s.write(None)



  //}
//}
