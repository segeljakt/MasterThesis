
case class Arg[T]()
case class Exp[T]()
case class Box[T]()
trait X


//implicit def xToAny[A](x: X): A = null
/*implicit class t[A](v: A) {
  def x(): 
}
*/
def fn[A](a: Arg[Int],
          b: Arg[Box[X]],
          c: Arg[X])
          (implicit ev: X => A = (_:X) => null) = {}
  
fn(Arg[Int],Arg[Box[Char]],Arg[Char])
