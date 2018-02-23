

object ControlFlowOverload {

  class _If(a: Boolean, b: Int) {
    def apply(c: => Int) = {

    }
    def Else(c: => Int) = {
      
    }
  }

  def If(a: Boolean)(b: Int): _If = {
    new _If(a, b)
  }

  def main(args: Array[String]): Unit = {
    If (true) {
      1
    } Else {
      2
    }
  }
}
  //While(true) {
  //println("Hello");
  //}
  //def While(cond: => Boolean)(body: => Unit): Unit = {
    //while(cond) {
      //body
    //}
  //}

