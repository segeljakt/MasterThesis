





object Example {

  def run(f: ()(implicit i: Int) => Unit): Unit = {
    implicit val i: Int = 3
    f()
  }

  def main(args: Array[String]): Unit =
    run {
      todo
    }

  def todo()(implicit i: Int): Unit =
    println(i)
} 
