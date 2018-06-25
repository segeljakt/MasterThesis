
  //* Imports
  import shapeless._
  import shapeless.syntax.singleton._
  import shapeless.record._
  import shapeless.labelled._
  import shapeless.ops.record._
  import shapeless.ops.hlist.LiftAll
  import shapeless.ops.hlist.ToTraversable
  import shapeless.syntax.singleton._
  import scala.reflect.runtime.universe._
  import scala.language.higherKinds
  import scala.language.implicitConversions
  import scala.language.postfixOps
  import scala.language.existentials

  //* Typelevel - X == Y
  type ==[A] = {
    type λ[B] = =:=[A,B]
  }
  //* Typelevel - X != Y
  trait <:!<[A,B]
  object <:!< {
    implicit def NotSubtype[A, B]      : A <:!< B = null
    implicit def Ambiguous1[A, B >: A] : A <:!< B = null
    implicit def Ambiguous2[A, B >: A] : A <:!< B = null
  }
  //* Typelevel - !X, !!X
  type ¬[X] = X => Nothing
  type ¬¬[X] = ¬[¬[X]]
  //* Typelevel - X = (Y | Z | ...)
  trait Disjunction[T] {
    type U[S] = Disjunction[T with ¬[S]]
    type λ[S] = ¬¬[S] <:< ¬[T]
  }
  type ∈[T] = {
    type U[S] = Disjunction[¬[T]]#U[S]
  }
  //* Typelevel - X != (Y | Z | ...)
  trait NotDisjunction[T] {
    type U[S] = NotDisjunction[T with ¬[S]]
    type λ[S] = ¬¬[S] <:!< ¬[T]
  }
  type ∉[T] = {
    type U[S] = NotDisjunction[¬[T]]#U[S]
  }
  //* Typelevel - Default
  trait Default[T, D]; object Default {
    implicit def try_1st[T]:    Default[T, T] = null
    implicit def try_2nd[T, D]: Default[T, D] = null
  }
  type Df[D] = {
    type λ[T] = Default[T, D]
  }
  //* ID-generator
  object IDGen {
    private[this] var counter: Int = 0
    def get = {
      counter += 1
      counter
    }
  }
  trait LowerCaseID {
    implicit val id: String = "x" + IDGen.get
  }
  trait UpperCaseID {
    implicit val id: String = "X" + IDGen.get
  }
  trait ExternalID {
    implicit val id: String
  }
  //* Interpretations
  trait Interp {
    def show(): String
  }
  //* File
  case class File(stmts: Stm*) extends Interp {
    def show = stmts.map(x => x.show).mkString("\n")
  }
  //* Constructs
  trait Stm extends Interp
  trait Exp[T] extends Stm
  trait Pattern extends Interp
  trait Facade[T]
  //* Variable modifiers
  trait Mut; trait ImMut
  trait AsRef; trait AsVal
  //* Types
  trait i32; trait f32; trait bool; trait char; trait str; trait unit
  //* Typeclass - Show
  trait Show[T] {
    def show(): String
  }
  implicit def ShowUnit: Show[unit] =
    () => "()"
  implicit def ShowMut: Show[Mut] =
    () => "mut"
  implicit def ShowImMut: Show[ImMut] =
    () => ""
  implicit def showTerm[T](implicit ev: TypeTag[T]): Show[T] =
    () => s"${ev.tpe.dealias.typeSymbol.name.toString}"
  implicit def ShowCompoundTerm[A[_], B <: HList, C <: HList]
                               (implicit
                                 ev0: TypeTag[A[B]],
                                 ev1: LiftAll.Aux[Show, B, C],
                                 ev2: ToTraversable.Aux[C, List, Show[_]]
                               ): Show[A[B]] =
  () => {
    val name = ev0.tpe.dealias.typeSymbol.name.toString
    val list = ev1.instances.toList[Show[_]]
    s"$name<${list.map(x=>x.show).mkString(", ")}>"
  }
  //* ItemDecl
  trait ItemDecl extends Stm with UpperCaseID {
    val bounds = ""
    val attrs = ""
  }
  //* Use
  case class Use(s: String) extends Stm {
    def show = s"use $s"
  }
  //* Variables
  trait Var[T] extends Stm with LowerCaseID {
    def read = Read[T](id)
    def write(e: Exp[T]) = Write[T](e, id)
  }
  case class Write[T](e: Exp[T], id: String) extends Exp[T] {
    def show = s"$id = ${e.show}"
  }
  case class Read[T](id: String) extends Exp[T] {
    def show = id
  }
  //* Expression - Block
  case class Block[T](v: Exp[T]) extends Exp[T] {
    val show: String = s"{\n  ${v.show}\n}"
  }
  case class ImplicitBlock[T](l: Stm, r: Exp[T]) extends Exp[T] {
    val show: String = s"${l.show};\n${r.show}"
  }
  implicit class ExpOperators[T](val r: Exp[T]) {
    def #:(l: Stm): ImplicitBlock[T] = ImplicitBlock(l, r)
  }
  implicit class StmOperators[T](val l: Stm) {
    def :# = ImplicitBlock(l, NoOp())
  }
  //* Expression - Literals
  case class Lit[T](v: String) extends Exp[T] {
    def show = v
  }
  //* Expression - Tuples
  trait Tup[T]
  implicit def ShowTupType[T <: HList, S <: HList]
                          (implicit
                           ev1: LiftAll.Aux[Show, T, S],
                           ev2: ToTraversable.Aux[S, List, Show[_]]
                          ): Show[Tup[T]] =
    () => s"(${ev1.instances.toList[Show[_]].map(x=>x.show).mkString(", ")})"
  case class TupExp[T](t: T) extends Exp[Tup[T]] {
    def show = ""
  }
  //case class TupExp[T <: HList](t: T) extends Exp[Tup[T]] {
    //def show = "tup"
  //}
  //* Expression - Binary operators
  case class Add[T](l: Exp[T], r: Exp[T]) extends Exp[T] {
    def show = s"(${l.show}+${r.show})"
  }
  //* Expression - Unary operators
  case class Not(e: Exp[bool]) extends Exp[bool] {
    def show = s"!${e.show}"
  }
  //* Expression - NoOp
  case class NoOp() extends Exp[unit] {
    def show = ""
  }
  //* Expression - If, IfElse
  case class If[T](cond: Exp[bool])(th: Block[T]) extends Exp[T] {
    def Else(el: Block[T]) = IfElse(cond)(th)(el)
    def show = s"if ${cond.show} {\n${th.show}\n}"
  }
  case class IfElse[T](cond: Exp[bool])(th: Block[T])(el: Block[T]) extends Exp[T] {
    def show = s"if ${cond.show} {\n${th.show}\n} else {\n${el.show}\n}"
  }
  //* Expression - Loops
  case class Loop[T](body: Block[T]) extends Exp[T] {
    def show = s"loop {\n${body.show}\n}"
  }
  case class While[T](cond: Exp[bool])(body: Block[T]) extends Exp[T] {
    def show = s"while ${cond.show} {\n${body.show}\n}"
  }
  case class WhileLet[T](cond: Exp[bool])(body: Block[T]) extends Exp[T] {
    def show = s"while ${cond.show} {\n${body.show}\n}"
  }
  case class For[T](body: Block[T]) extends Exp[T] {
    def show = s"for {\n${body.show}\n}"
  }
  //* Expression - Match
  case class Match[T](exp: Exp[_])(cases: Case[T]*) extends Exp[T] {
    def show = s"match ${exp.show} {${cases.map(x => x.show).mkString(",\n")}}"
  }
  case class Case[T](pat: Pattern, exp: Exp[T]) extends Exp[T] {
    def show = s"${pat.show} => {${exp.show}}"
  }
  //* Expression - Ref / Deref
  trait Reference[T]
  case class Ref[T](v: Exp[T]) extends Exp[Reference[T]] {
    def show = s"&${v.show}"
  }
  case class Deref[T](v: Exp[Reference[T]]) extends Exp[T] {
    def show = s"*${v.show}"
  }
  //* Patterns
  case class Wildcard() extends Pattern {
    def show = "_"
  }
  //* Statement/Expression - Functions
  case class FnCall[T : Df[unit]#λ](args: Exp[_]*)(implicit id: String) extends Exp[T] {
    def show = s"$id(${args.map(x => x.show).mkString(",")})"
  }
  case class Arg[T]()(implicit ev: Show[T]) extends Var[T] {
    def show = s"${id}: ${ev.show}"
  }
  case class SelfRefArg[M,T]()(implicit ev: Show[M]) extends Var[T] {
    override implicit val id = "self"
    override def show = s"&${ev.show} self"
  }
  trait FnDecl extends ItemDecl {
    val args: List[Arg[_]] = List()
    val body: Exp[_]
    val ret: Return[_]
    def show = s"fn $id$bounds(${args.map(x => x.show).mkString(",")}) -> ${ret.show} {\n${body.show}\n}"
  }
  case class Return[T]()(implicit ev: Show[T]) extends Stm {
    def show = ev.show
  }
  trait FnFacade extends ExternalID
  trait MethodDecl extends ItemDecl with TraitItem  {
    val self: SelfRefArg[_,_]
    val args: List[Arg[_]] = List()
    val body: Option[Exp[_]]
    val ret: Return[_]
    def show = {
      var s = s"fn $id$bounds(${self.show},${args.map(x => x.show).mkString(",")}) -> ${ret.show}"
      body match {
        case Some(b) => s+= s" {\n${b.show}\n}"
        case None    => s+= ";"
      }
      s
    }
  }
  abstract class StaticMethodDecl(implicit val structId: String) extends TraitItem with UpperCaseID {
    val bounds = ""
    val args: List[Arg[_]] = List()
    val body: Option[Exp[_]]
    val ret: Return[_]
    def show = {
      var s = s"fn $id$bounds(${args.map(x => x.show).mkString(",")}) -> ${ret.show}"
      body match {
        case Some(b) => s+= s" {\n${b.show}\n}"
        case None    => s+= ";"
      }
      s
    }
  }
  case class MethodCall[T](s: Exp[_], m: MethodDecl, args: Exp[_]*) extends Exp[T] {
    def show = s"${s.show}::${m.id}(${args.map(x => x.show).mkString(",")})"
  }
  case class StaticMethodCall[T](m: StaticMethodDecl, args: Exp[_]*) extends Exp[T] {
    def show = s"${m.structId}::${m.id}(${args.map(x => x.show).mkString(",")})"
  }
  case class TypeAlias[From,To]()(implicit from: Show[From], to: Show[To]) extends TraitItem {
    def show = s"type ${from.show} = ${to.show};"
  }
  case class ExternCrate(name: String) extends Stm {
    def show = s"extern crate $name;"
  }
  case class MacroCall[T : Df[unit]#λ](s: String, stmts: Stm*) extends Exp[unit] {
    def show = s"$s(${stmts.map(x => x.show).mkString(",")})"
  }
  case class ClosureArg[T]() extends Var[T] {
    override def show = s"${id}"
  }
  case class Closure[T](args: Var[_]*)(exp: Exp[T]) extends Exp[T] {
    def show = s"|${args.map(x => x.show).mkString(",")}| ${exp.show}"
  }
  case class MoveClosure[T](args: Arg[_]*)(exp: Exp[T]) extends Exp[T] {
    def show = s"move |${args.map(x => x.show).mkString(",")}| ${exp.show}"
  }
  //* Traits
  trait Trait extends UpperCaseID {
    val bounds: String = ""
  }
  trait TraitDecl extends ItemDecl {
    val methods: List[MethodDecl]
        def show = s"trait $id$bounds {\n${methods.map(x => x.show).mkString("\n")}\n}"
  }
  trait TraitItem extends Stm
  abstract class Impl[T,S](implicit t: Show[T], s: Show[S]) extends Stm {
    val implbounds: String = ""
    def show = s"impl$implbounds ${t.show} for ${s.show} {\n${items.map(x => x.show).mkString("\n")}}"
    val items: List[TraitItem]
  }
  abstract class ImplStruct[S](implicit s: Show[S]) extends Stm {
    implicit val id: String = s.show
    val implbounds: String = ""
    val items: List[TraitItem]
    def show = s"impl$implbounds ${s.show} {\n${items.map(x => x.show).mkString("\n")}}"
  }
  case class Attribute(s: String) extends Stm {
    def show = s"\n$s\n"
  }
  //* Statement/Expression - Structs
  trait Struct
  trait ShowStructExp[T <: Struct] {
    def show(exps: Seq[Exp[_]]): String
  }
  trait ShowStructDecl[T <: Struct] {
    def show(bounds: String): String
  }
  implicit def showStructField[K,V](implicit wk: Witness.Aux[K], ev: Show[V]): Show[FieldType[K,V]] =
    () => s"${wk.value.toString}: ${ev.show}"
  implicit def showStructDecl[S <: Struct, F <: HList, FS <: HList]
                             (implicit
                              ev0: TypeTag[S],
                              ev1: Fields.Aux[S, F],
                              ev2: LiftAll.Aux[Show, F, FS],
                              ev3: ToTraversable.Aux[FS, List, Show[_]]
                             ): ShowStructDecl[S] =
    (bounds: String) => {
      val name = ev0.tpe.dealias.typeSymbol.name.toString
      val keytypes = ev2.instances.toList[Show[_]].map(x=>x.show).mkString(",\n")
      s"struct $name$bounds {\n$keytypes\n}"
    }
  implicit def showStructExp[S <: Struct, F <: HList, K <: HList, W <: HList]
                            (implicit
                             ev0: WeakTypeTag[S],
                             ev1: Fields.Aux[S, F],
                             ev2: Keys.Aux[F, K],
                             ev3: LiftAll.Aux[Witness.Aux, K, W],
                             ev4: ToTraversable.Aux[W, List, Witness.Aux[_]]
                            ): ShowStructExp[S] =
    (exps: Seq[Exp[_]]) => {
      val name = ev0.tpe.dealias.typeSymbol.name.toString
      val keys = ev3.instances.toList.map(x=>x.value.toString)
      val keyvals = keys.zip(exps).map{ case (k,e) => s"$k: ${e.show}"}.mkString(",\n")
      s"$name {\n$keyvals\n}"
    }
  case class StructExp[S <: Struct](exps: Exp[_]*)(implicit ev: ShowStructExp[S]) extends Exp[S] {
    def show = ev.show(exps)
  }
  case class StructDecl[S <: Struct](bounds: String = "")(implicit ev: ShowStructDecl[S]) extends Stm {
    def show = ev.show(bounds)
  }
  implicit class StructAccess[S <: Struct, F <: HList](s: Exp[S])(implicit ev: Fields.Aux[S, F]) {
    def get[V](k: Witness)(implicit selector: Selector.Aux[F, k.T, V]) = GetField[V](s, k.value.toString)
    def set[V](k: Witness, v: Exp[V])(implicit selector: Selector.Aux[F, k.T, V]) = SetField(s, k.value.toString, v)
  }
  case class GetField[T](base: Exp[_], id: String) extends Exp[T] {
    def show = s"${base.show}.$id"
  }
  case class SetField(base: Exp[_], id: String, v: Exp[_]) extends Stm {
    def show = s"${base.show}.$id = ${v.show}"
  }
  //* Statement/Expression - UnitStruct
  trait UnitStruct extends Struct
  abstract class UnitStructDecl[S <: UnitStruct](implicit ev: Show[S]) {
    object Decl extends Stm {
      def show = s"struct ${ev.show};"
    }
    def apply() = new Exp[S] {
      def show = ev.show
    }
  }
  //* Statement/Expression - Fields
  trait Fields[S <: Struct] {
    type F
  }
  object Fields {
    type Aux[S <: Struct, F0] = Fields[S] { type F = F0 }
    def apply[S <: Struct](implicit ev: Fields[S]): Aux[S, ev.F] = ev
  }
  //* Statement - Let
  case class Let[T](e: Exp[T]) extends Var[T] {
    def show = s"let ${id} = ${e.show}"
  }
  case class LetMut[T](e: Exp[T]) extends Var[T] {
    def show = s"let mut ${id} = ${e.show}"
  }
  
  //* EXAMPLE --------------------------------------------------------------------
  
  //* Facades
  
  trait Port {
    trait Indication
    trait Request
  }
  trait ComponentContext; object ComponentContext {
    def New() = Lit[ComponentContext]("ComponentContext::new()")
  }
  trait RequiredPort[T]; object RequiredPort {
    def New[A,B]() = Lit[RequiredPort[A::B::HNil]]("RequiredPort::new()")
  }
  implicit class RequiredPortAccess[A,B](v: Exp[RequiredPort[A::B::HNil]]) {
    def share = Lit[unit](s"${v.show}.share()")
  }
  trait ProvidedPort[T]; object ProvidedPort {
    def New[A,B]() = Lit[ProvidedPort[A::B::HNil]]("ProvidedPort::new()")
  }
  trait ControlPort
  trait ControlEvent; object ControlEvent {
    case class Start() extends Pattern {
      def show = "ControlEvent::Start"
    }
  }
  trait KompicsConfig; object KompicsConfig {
    def New() = Lit[KompicsConfig]("KompicsConfig::new()")
  }
  implicit class KompicsConfigAccess(v: Exp[KompicsConfig]) {
    def throughput(t: Exp[i32]) = Lit[unit](s"${v.show}.throughput(${t.show})")
  }
  trait KompicsSystem; object KompicsSystem {
    def New(v: Exp[KompicsConfig]) = Lit[KompicsSystem](s"KompicsSystem::new(${v.show})")
  }
  implicit class KompicsSystemAccess(v: Exp[KompicsSystem]) {
    def create(r: MoveClosure[_]) = Lit[Component](s"${v.show}.create(${r.show})")
    def start(r: Exp[_]) = Lit[unit](s"${v.show}.start(${r.show})")
    def trigger_i(a: Exp[_], b: Exp[_]) = Lit[unit](s"${v.show}.trigger_i(${a.show}, ${b.show})")
  }
  trait Component
  implicit class ComponentAccess(v: Exp[Component]) {
    def on_definition(a: Closure[_]) = Lit[unit](s"${v.show}.on_definition(${a.show})")
  }
  object thread {
    def sleep(v: Exp[i32]) = Lit[unit](s"thread::sleep(${v.show})")
  }
  object time {
    object Duration {
      def from_millis(v: Exp[i32]) = Lit[i32](s"time::Duration::from_millis(${v.show})")
    }
  }
  trait Provide[T] {
    trait Handle extends MethodDecl {
      override implicit val id = "handle"
    }
  }
  trait Require[T] {
    trait Handle extends MethodDecl {
      override implicit val id = "handle"
    }
  }

  //* Implementation

  trait Ping extends UnitStruct; object Ping extends UnitStructDecl[Ping]
  trait Pong extends UnitStruct; object Pong extends UnitStructDecl[Pong]
  trait PingPongPort extends UnitStruct; object PingPongPort extends UnitStructDecl[PingPongPort]
  trait Pinger extends Struct; object Pinger {
    type F1 = ComponentContext
    type F2 = RequiredPort[PingPongPort::Pinger::HNil]
    type F3 = ProvidedPort[PingPongPort::Pinger::HNil]
    type F4 = i32
    type F = Record.`"ctx"->F1, "ppp"->F2, "pppp"->F3, "test"->F4`.T
    implicit def fields = new Fields[Pinger] {
      type F = Record.`"ctx"->F1, "ppp"->F2, "pppp"->F3, "test"->F4`.T
    }
    def apply(ctx: Exp[F1], ppp: Exp[F2], pppp: Exp[F3], test: Exp[F4]) =
      StructExp[Pinger](ctx, ppp, pppp, test)
    object Decl extends StructDecl[Pinger]
    def New = StaticMethodCall[Pinger](ImplPinger.New)
  }
  object ImplPortForPingPongPort extends Impl[Port, PingPongPort] with Port {
    object Indication extends TypeAlias[Indication, Pong]
    object Request extends TypeAlias[Request, Ping]
    val items = List(Indication, Request)
  }
  object ImplPinger extends ImplStruct[Pinger] {
    object New extends StaticMethodDecl {
      val ret = Return[Pinger]
      val body = Some(
        Pinger(
          ComponentContext.New(),
          RequiredPort.New[PingPongPort, Pinger](),
          ProvidedPort.New[PingPongPort, Pinger](),
          Lit[i32]("0")
        )
      )
    }
    val items = List(New)
  }
  object ImplProvideForPinger extends Impl[Provide[ControlPort::HNil], Pinger] with Provide[ControlPort::HNil] {
    object Handle extends Handle {
      val self = SelfRefArg[Mut, Pinger]
      val event = Arg[ControlEvent]
      override val args = List(event)
      val ret = Return[unit]
      val body = Some(
        Match(event.read)(
          Case(ControlEvent.Start(),
            MacroCall("println!", Lit("\"Starting Pinger... {}\""), self.read.get("test"))
          ),
          Case(Wildcard(),
            NoOp()
          )
        )
      )
    }
    val items = List(Handle)
  }
  implicit class ImplProvideForPinger(s: Exp[Pinger]) {
    def Handle(a1: Exp[Pinger], a2: Exp[ControlEvent]) = MethodCall[unit](s, ImplProvideForPinger.Handle, a1, a2)
  }
  object ImplRequireForPinger extends Impl[Require[PingPongPort::HNil], Pinger] with Require[PingPongPort::HNil] {
    object Handle extends Handle {
      val self = SelfRefArg[Mut, Pinger]
      val event = Arg[Pong]
      override val args = List(event)
      val ret = Return[unit]
      val body = Some(MacroCall("println!", Lit("\"Got a pong!\"")))
    }
    val items = List(Handle)
  }
  object ImplProvideForPinger2 extends Impl[Provide[PingPongPort::HNil], Pinger] with Provide[PingPongPort::HNil] {
    object Handle extends Handle {
      val self = SelfRefArg[Mut, Pinger]
      val event = Arg[Ping]
      override val args = List(event)
      val ret = Return[unit]
      val body = Some(MacroCall("println!", Lit("\"Got a ping!\"")))
    }
    val items = List(Handle)
  }
  object Main extends FnDecl {
    override val id = "main"
    val ret = Return[unit]
    val body = {
      val conf = LetMut(KompicsConfig.New())
      val system = Let(KompicsSystem.New(conf.read))
      val pingerc = LetMut(system.read.create(MoveClosure()(Pinger.New)))
      conf #:
      Block(
        conf.read.throughput(Lit[i32]("5"))
      ) #:
      system #:
      pingerc #:
      system.read.start(Ref(pingerc.read)) #:
      system.read.trigger_i(Pong(), pingerc.read.on_definition{
        val a = ClosureArg[Pinger]
        Closure(a)(a.read.get("ppp").share)
      }) #:
      thread.sleep(time.Duration.from_millis(Lit[i32]("5000"))) :#
    }
  }
  
  println(
    File(
      ExternCrate("kompics"),
      Use("kompics::*;"),
      Use("std::sync::Arc;"),
      Use("std::{thread, time};"),
      Lit("#[derive(Clone, Debug)]"),
      Ping.Decl,
      Lit("#[derive(Clone, Debug)]"),
      Pong.Decl,
      PingPongPort.Decl,
      ImplPortForPingPongPort,
      Lit("#[derive(ComponentDefinition, Actor)]"),
      Pinger.Decl,
      ImplPinger,
      ImplProvideForPinger,
      ImplRequireForPinger,
      ImplProvideForPinger2,
      Main
    ).show
  )
