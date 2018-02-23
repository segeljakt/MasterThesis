test("unroll") {
  val snippet = new DslDriver[Int,Unit] {
    def snippet(x: Rep[Int]) = comment("for", verbose = false) {

      for (i <- (0 until 3): Range) {
        println(i)
      }

    }
  }
  check("unroll", snippet.code)
}

import scala.language.experimental.macros
def printf(format: String, params: Any*): Unit = macro printf_impl
