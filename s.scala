trait Ping
trait Pong
trait PingPongPort
trait Pinger[T]

object Ping extends UnitStructDecl[Ping]
object Pong extends UnitStructDecl[Pong]
object PingPongPort extends UnitStructDecl[PingPongPort]
object Pinger extends StructDecl[Pinger] {
  override implicit val id = "Pinger"
  implicit val fields = List(
    Field[ComponentContext],
    Field[RequiredPort[(PingPongPort, Pinger[HNil])]],
    Field[ProvidedPort[(PingPongPort, Pinger[HNil])]],
    Field[i32]
  )
  def apply(ctx:  Exp[ComponentContext],
            ppp:  Exp[RequiredPort[(PingPongPort, Pinger[HNil])]],
            pppp: Exp[ProvidedPort[(PingPongPort, Pinger[HNil])]],
            test: Exp[i32]) = StructExp[Pinger[HNil]](ctx, ppp, pppp)
}

implicit class PingerAccess(s: Exp[Pinger[HNil]]) {
  def ppp = ReadField[RequiredPort[(PingPongPort, Pinger[HNil])]](s, Pinger.fields(1))
}

object ImplPortForPingPongPort extends Impl[Pinger[HNil],Port] with Port {
  object IndicationAlias extends TypeAlias[Indication, Pong]
  object RequestAlias extends TypeAlias[Request, Ping]
  val items = List(IndicationAlias, RequestAlias)
}

object ImplPinger extends ImplStruct[Pinger[HNil]] {
  object New extends StaticMethodDecl {
    override implicit val id = "new"
    val args = List()
    val ret = Return[Pinger[HNil]]
    val body = Some(Pinger(
      ComponentContext.New(),
      RequiredPort.New[PingPongPort,Pinger[HNil]](),
      ProvidedPort.New[PingPongPort,Pinger[HNil]](),
      Lit[i32]("0")
    ))
  }
  val items = List(New)
}

implicit class PingerStaticAccess(s: StructDecl[Pinger]) {
  def New = StaticMethodCall[Pinger[HNil]](s, ImplPinger.New)
}

object ImplProvideForPinger extends Impl[Pinger[HNil],Provide[ControlPort]] with Provide[ControlPort] {
  object HandleDecl extends Handle {
    val self = SelfRefArg[Mut,Pinger[HNil]]
    val event = Arg[ControlEvent]
    val args = List(event)
    val ret = Return[unit]
    val body = Some(Match(event.read,
      Case(ControlEvent.Start(),
        MacroCall("println!(\"Starting Pinger... {}\", self.test)") :#
      ),
      Case(Wildcard(),
        NoOp()
      )
    ))
  }
  val items = List(HandleDecl)
}
implicit class ImplProvideForPinger(s: Exp[Pinger[HNil]]) {
  def Handle(a1: Exp[Pinger[HNil]], a2: Exp[ControlEvent]) = MethodCall[unit](s, ImplProvideForPinger.items(0), a1, a2)
}

object ImplRequireForPinger extends Impl[Pinger[HNil],Require[PingPongPort]] with Require[PingPongPort] {
  object HandleDecl extends Handle {
    val self = SelfRefArg[Mut,Pinger[HNil]]
    val event = Arg[Pong]
    val args = List(event)
    val ret = Return[unit]
    val body = Some(MacroCall("println!(\"Got a pong!\")"))
  }
  val items = List(HandleDecl)
}

object ImplProvide2ForPinger extends Impl[Pinger[HNil],Provide[PingPongPort]] with Provide[PingPongPort] {
  object HandleDecl extends Handle {
    val self = SelfRefArg[Mut,Pinger[HNil]]
    val event = Arg[Pong]
    val args = List(event)
    val ret = Return[unit]
    val body = Some(MacroCall("println!(\"Got a ping!\")"))
  }
  val items = List(HandleDecl)
}

object Main extends FnDecl {
  override val id = "main"
  val args = List()
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
      val a = Arg[Pinger[HNil]]
      Closure(a)(a.read.ppp.share)
    }) #:
    thread.sleep(time.Duration.from_millis(Lit[i32]("5000"))) :#
  }
}

println(File(
  ExternCrate("kompics"),
  Lit("use kompics::*;"),
  Lit("use std::sync::Arc;"),
  Lit("use std::{thread, time};"),
  Lit("use kompics::*;"),
  Lit("#[derive(Clone,Debug)]"),
  Ping,
  Lit("#[derive(Clone,Debug)]"),
  Pong,
  PingPongPort,
  Lit("#[derive(ComponentDefinition,Actor)]"),
  Pinger,
  ImplPortForPingPongPort,
  ImplPinger,
  ImplProvideForPinger,
  ImplRequireForPinger,
  ImplProvide2ForPinger,
  Main
).show)
