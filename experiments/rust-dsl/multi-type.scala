





object Emit {
  class Exp()
  class Lit(var value: Int) extends Exp
  class Add[L <: Exp, R <: Exp](var lhs: L, var rhs: R) extends Exp

  def emit[L,R](node: Add[L,R]) {
    emit(node.lhs);
  }
  //def emit(node: Lit) {
    //print(node.value);
  //}
}
