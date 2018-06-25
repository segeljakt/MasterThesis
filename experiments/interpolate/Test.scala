object Test extends App {
  //import scala.language.experimental.macros
  import Rust._

  val x = "i32"
  val y = ";"
  val z = "f32"
  val r = rruste"(1+2)"
  val c = cruste"(1+2)"
  // Test 1
  //assert(rrust"fn main() { let x: $x = 3; }" == "fn main() { let x: i32 = 3; }")
  //assert(rrusti"fn main() { let x: $x = 3; }" == "fn main() { let x: i32 = 3; }")
  //assert(crust"fn main() { let x: $x = 3; }" == "fn main() { let x: i32 = 3; }")
  //assert(crusti"fn main() { let x: $x = 3; }" == "fn main() { let x: i32 = 3; }")
  // Test 2
  //rrust"fn main() { let x: $y = 3; }"
  //rrusti"fn main() { let x: $y = 3; }"
  //crust"fn main() { let x: $y = 3; }"
  //crusti"fn main() { let x: $y = 3; }"
  //// Test 3
  //rrust"fn main() { let x: $z = 3; }"
  //rrusti"fn main() { let x: $z = 3; }"
  //crust"fn main() { let x: $z = 3; }"
  //crusti"fn main() { let x: $z = 3; }"
  //// Test 4
  rrust"fn main() { let x = $r; }"
  rrusti"fn main() { let x = $r; }"
  crust"fn main() { let x = $c; }"
  crusti"fn main() { let x = $c; }"
  


}
