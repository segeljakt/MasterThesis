











object ControlFlowOverload {

  case class If(a: Boolean)(b: Int) {
    def Else(c: => Int): If = {
      this
    }
  }

  def main(args: Array[String]): Unit = {
    If (true) {
      1
    } Else If (true) {
      2
    }
  }

}
