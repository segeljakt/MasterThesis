










object A {
  implicit class I1(i: Int) {
    def myPrint() {
      println(i)
    }
  }
  implicit class I2(i: String) {
    def myPrint() {
      println(i)
    }
  }
}
 
import A._ ;
 
object MyApp extends App {
  3.myPrint()
  Let x = RInt 3
  val t01 = Let("x",Rint(3))
  seq.add(t01)
  val x = t01.getAccess()
  Let y = Rint 4
  x Add y
  "asd".myPrint()
}
