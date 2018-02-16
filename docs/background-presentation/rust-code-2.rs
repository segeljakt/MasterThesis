/******************************************************************************
*     File Name           :     rust-code-2.rs                                *
*     Created By          :     Klas Segeljakt <klasseg@kth.se>               *
*     Creation Date       :     [2018-02-16 18:00]                            *
*     Last Modified       :     [2018-02-16 18:55]                            *
*     Description         :                                                   *
******************************************************************************/
/* ------------------ Interface -------------------- */
enum Expr {
  Lit(Lit),
  Add(Add),
}
struct Lit {
  val: i32,
}
struct Add {
  lhs: Box<Expr>,
  rhs: Box<Expr>,
}
impl Expr {
/* --------------------- Eval ---------------------- */
  fn eval(&self) -> i32 {
    &Expr::Lit(node) => node.val,
    &Expr::Add(node) => *node.lhs.eval() + *node.rhs.eval(),
  }
/* --------------------- Emit ---------------------- */
  fn emit(&self) -> String {
    &Expr::Lit(node) => node.val.to_string(),
    &Expr::Add(node) => format!("{} + {}",
                               *node.lhs.emit(),
                               *node.rhs.emit()),
  }
}
fn main() { }


//fn eval(node: Expr) -> i32 {
  //match node {
    //Expr::Lit(node) => node.val,
    //Expr::Add(node) => eval(*node.lhs) + eval(*node.rhs),
  //}
//}
//fn emit(node: Expr) -> String {
  //match node {
    //Expr::Lit(node) => node.val.to_string(),
    //Expr::Add(node) => format!("{} + {}",
                               //emit(*node.lhs),
                               //emit(*node.rhs)),
  //}
//}
