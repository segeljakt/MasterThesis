







object Loops {

  import scala.collection.mutable.ListBuffer

  type Sequence = ListBuffer[Int];

  def Let(value: Int)(implicit s: ListBuffer[Int]) = {
    s += value
  }

  def While(cond: => Boolean)(body: Int): Int = {
    body
  }

  def main(args: Array[String]): Unit = {
    val x = While(true) {
      implicit val seq = new Sequence()
      Let(3)
      seq
    }
    println(x)
  }
}
