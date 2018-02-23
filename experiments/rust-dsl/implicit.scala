







object Loops {

  import scala.collection.mutable.ListBuffer

  def While(cond: => Boolean)(body: Int): Int = {
    body
  }


type Sequence = ListBuffer[Int];

def Let(value: => Int)(implicit s: ListBuffer[Int]): Int = {
  s += value
  value
}

def main(args: Array[String]): Unit = {
  implicit val seq = new Sequence()
  val x = Let(3)
  val y = Let(3)
  val z = Let(x+y)
  println(seq) // Prints 
}
  
}
