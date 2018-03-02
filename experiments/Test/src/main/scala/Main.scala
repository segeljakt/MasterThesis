
object Main {
  def main(): Unit = {
    // language=Rust
    val s =
      """
      fn main() {
        x()
      }
      fn x(q: i32) {
        return q
      }
      """
    println(s)
  }
}
