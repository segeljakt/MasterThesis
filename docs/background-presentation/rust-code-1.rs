/* --------------- Interface ----------------- */
trait Interp {
  fn eval(&self) -> i32;
  fn emit(&self) -> String;
}
/* ----------------- Literal ----------------- */
struct Lit {
  val: i32,
}
impl Interp for Lit {
  fn eval(&self) -> i32 {
    self.val
  }
  fn emit(&self) -> String {
    self.val.to_string()
  }
}
/* ------------------- Add ------------------- */
struct Add<L:Interp,R:Interp> {
  lhs: L,
  rhs: R,
}

impl<L:Interp,R:Interp> Interp for Add<L,R> {
  fn eval(&self) -> i32 {
    self.lhs.eval() + self.rhs.eval()
  }
  fn emit(&self) -> String {
    format!("{} + {}", self.lhs.emit(), self.rhs.emit())
  }
}

fn main() { }
