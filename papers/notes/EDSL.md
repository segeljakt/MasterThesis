# Combining Deep and Shallow Embedding for EDSL

There is a tradeoff between deep and shallow embeddings. Deep embeddings which represent a program as an AST can be compiled and optimized. The AST can grow large and complex to accommodate for new language features. This results in a DSL which is difficult to program. Shallow embeddings which directly translate from constructs to semantics are easier to implement. Although, by not having an AST, the DSL code cannot be optimized and verified.

Their tradeoff referred to as the *expression problem*. Deep embeddings are flexible in adding new interpretations, but inflexible in adding new constructs. Adding a new construct requires updating all existing interpretations. It is the opposite for shallow embeddings. Because the semantic domain is fixed in shallow embeddings, the representation dictates what operations can be performed. Thereby, there is essentially only one interpretation. Adding a new interpretation would therefore require updating all existing constructs.

It is possible to combine the two embedding styles and keep most of their strengths. This hybrid style is referred to as a deep embedding with shallow derived constructs. Derived constructs

Another approach is the Finally Tagless technique. It uses an interface to abstract over all interpretations. The interface can be implemented to create different concrete interpretations. New constructs and interpretations can be added with ease. Constructs are added by supplementing the interface with new methods. Interpretations are added by creating new instances of the interface.


Keywords: Embedded DSL, Higher Order Abstract Syntax (Constructs with variable binding)


DSL

  inRegion :: Point  -> Region -> Bool
  circle   :: Radius -> Region
  outside  :: Region -> Region
  (∩)      :: Region -> Region -> Region
  (∪)      :: Region -> Region -> Region


Shallow

  type Region = Region -> Bool

  p 'inRegion' r = r p
  circle  r      = λp -> magnitude p ≤ r
  outside r      = λp -> not (r p)
  r1 ∩ r2        = λp -> r1 p && r2 p
  r1 ∪ r2        = λp -> r1 p || r2 p


Deep

  data Region = Circle  Radius | Intersect Region Region
              | Outside Region | Union     Region Region

  circle  r = Circle    r
  outside r = Outside   r
  r1 ∩ r2   = Intersect r1 r2
  r1 ∪ r2   = Union     r1 r2

  p 'inRegion' (Circle  r)       = magnitude p ≤ r
  p 'inRegion' (Outside r)       = not (p 'inRegion' r)
  p 'inRegion' (Intersect r1 r2) = p 'inRegion' r1 && p 'inRegion' r2
  p 'inRegion' (Union     r1 r2) = p 'inRegion' r1 || p 'inRegion' r2




EXPRESSION PROBLEM

Rust:

```rust

  trait Exp {
    fn eval() -> i32;
    fn emit() -> String;
  }

  /* -------------------- Literal -------------------- */

  struct Literal {
    val: i32,
  }

  impl Exp for Literal {
    fn eval(&self) -> i32 {
      self.val
    }
    fn emit(&self) -> String {
      self.val.to_string()
    }
  }

  /* ---------------------- Add ---------------------- */

  struct Add<T: Exp> {
    lhs: T,
    rhs: T,
  }

  impl<T: Exp> Exp for Add<T> {
    fn eval(&self) -> i32 {
      self.lhs.eval() + self.rhs.eval()
    }
    fn emit(&self) -> String {
      format!("{} + {}", self.lhs.emit(), self.rhs.emit())
    }
  }
```

Hard to add operations: New operations requires changing Const and Add.
Easy to add types: Just implement Exp for the new type.


Haskell:

```haskell
  module Expressions where

  data Expression = Const Int | Add Exp Exp

  eval :: Exp -> Int
  eval (Const c) = c
  eval (Add lhs rhs) = eval lhs + eval rhs

  to_string :: Exp -> String
  to_string :: (Const c) = show c
  to_string :: (Add lhs rhs) = to_string lhs ++ " + " ++ to_string rhs
```

Easy to add operations: Just implement an operation for each type.
Hard to add types: New types requires changing eval and to_string.

Types <=> Constructs
Operations <=> Interpretations

Use traits, abstract over all interpretations.

```
  trait Interpretation {
    fn generate();
    fn evaluate();
    fn compile();
    ..
  }
```

Alternatively, one abstract interface for each interpretation

```
  trait Generate: Interpretation {
    fn generate();
  }
  ..
```

Then, each construct extends the interpretations:

```
  struct Integer: Interpretation {
    fn generate() { .. }
    fn evaluate() { .. }
    fn compile()  { .. }
  }
```

or:

```
  struct Integer: Generate {
    generate()..
  }
  ..
```

Object Oriented:
* Modularize by type/construct => Abstract operation/interpretation
Functional:
- Modularize by operation/interpretation => Abstract type/construct


# https://oleksandrmanzyuk.wordpress.com/2014/06/18/from-object-algebras-to-finally-tagless-interpreters-2/

## Object algebras:


```Java
  interface ExpAlg<T> {
    T lit(int n);
    T add(T x, T y);
  }

  <T> T e1(ExpAlg<T> f) {
    return f.add(
      f.lit(1),
      f.add(
        f.lit(2),
        f.lit(3)));
  }


  interface Eval { int eval(); }

  class EvalExp implements ExpAlg<Eval> {
    Eval lit(final int n) {
      return new Eval() {
        int eval() {
          return n;
        }
      };
    }
    Eval add(final Eval x, final Eval y) {
      return new Eval() {
        int eval() {
          return x.eval() + y.eval();
        }
      };
    }
  }

  int v1 = e1(new EvalExp()).eval();

  interface MulAlg<T> extends ExpAlg<T> {
    T mul(T x, T y);
  }

  <T> T e2(MulAlg<T> f) {
    return f.mul(
      f.lit(4),
      f.add(
        f.lit(5),
        f.lit(6)));
  }

  class EvalMul extends EvalExp implements MulAlg<Eval> {
    Eval mul(final Eval x, final Eval y) {
      return new Eval() {
        int eval() {
          return x.eval() * y.eval();
        }
      };
    }
  }

  int v2 = e2(new EvalMul()).eval();

  interface View { String view(); }

  class ViewExp implements ExpAlg<View> {
    View lit(final int n) {
      return new View() {
        String view() {
          return Integer.toString(n);
        }
      };
    }
    View add(final View x, final View y) {
      return new View() {
        String view() {
          return "(" + x.view() + " + " + y.view() + ")";
        }
      };
    }
  }

  class ViewMul extends ViewExp implements MulAlg<View> {
    View mul(final View x, final View y) {
      return new View() {
          String view() {
          return "(" + x.view() + " * " + y.view() + ")";
        }
      };
    }
  }

  String s1 = e1(new ViewExp()).view();
  String s2 = e2(new ViewMul()).view();
```

Basically:

```Java
  interface ExpAlg<T> {
    T lit(int n);
    T add(T x, T y);
  }

  interface MulAlg<T> extends ExpAlg<T> {
    T mul(T x, T y);
  }

  interface Eval { int eval(); }
  interface View { String view(); }

  class EvalExp implements ExpAlg<Eval> {
    // construct eval() for lit and add
  }

  class EvalMul extends EvalExp implements MulAlg<Eval> {
    // construct eval() for mul
  }

  class ViewExp implements ExpAlg<View> {
    // construct view() for lit and add
  }

  class ViewMul extends ViewExp implements MulAlg<View> {
    // construct view() for mul
  }
```

## Final Tagless

No need to make MulAlg subclass of ExpAlg

```haskell
  class ExpAlg t where
    lit :: Int -> t
    add :: t -> t -> t

  e1 = add (lit 1)
           (add (lit 2)
                (lit 3))

  newtype Eval = Eval { eval :: Int }

  instance ExpAlg Eval where
    lit n   = Eval n
    add x y = Eval $ eval x + eval y

  v1 = eval (e1 :: Eval)

  class ExpAlg t => MulAlg t where
    mul :: t -> t -> t

  e2 = mul(lit 4)
          (add (lit 5)
               (lit 6))

  instance MulAlg Eval where
    mul x y = Eval $ eval x * eval y

  newtype View = View { view :: String }

  instance ExpAlg View where
    lit n   = View $ show n
    add x y = View $ "(" ++ view x ++ " + " ++ view y ++ ")"

  instance MulAlg View where
    mul x y = View $ "(" ++ view x ++ " * " ++ view y ++ ")"

  class MulAlg t where
    mul :: t -> t -> t
```

```java
  class Eval { public int eval; }

  class EvalExp implements ExpAlg<Eval> {
    Eval lit(int n) {
      return Eval(n);
    }
    Eval add(Eval x, Eval y) {
      return Eval(x.eval + y.eval);
    }
  }

  int v1 = e1(new EvalExp()).eval;
```
