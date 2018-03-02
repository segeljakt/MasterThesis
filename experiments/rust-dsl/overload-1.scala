

object ControlFlowOverload {

  //class If {
    //var cond = false
    //var body = 0
    //var next = 0
    //def apply(cond: => Boolean): If = {
      //this.cond = cond
      //this
    //}
    //def apply(body: => Int): If = {
      //this.body = body
      //this
    //}
    //def Else(next: => Int) = {
      //this.next = next
    //}
  //}
  //object If {
    //def apply(): If = {
      //new If
    //}
  //}

  class If(a: Boolean, b: Int) {
    def Else(c: => Int): If = {
      this
    }
  }
  def If(a: Boolean)(b: Int): If = {
    new If(a, b)
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

