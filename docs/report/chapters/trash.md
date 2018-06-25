
A subset of Rust's current features are listed in table {@tbl:rust}. Many of the features are present in other programming languages. The defining features of Rust are ownership and lifetimes. These are explained in the following sections. Useful features include doc comments, which lets the user write documentation directly in the code in a markdown format. Cargo can afterwards generate `.pdf` files or wikis from the doc comments.

|                             |                      |                   |
|-----------------------------+----------------------+-------------------|
| Attributes                  | Mutability           | References        |
| Associated types            | **Lifetimes**        | Structs           |
| Closures                    | Macros               | Traits            |
| Control flow                | Modifiers            | Trait objects     |
| Doc comments                | Modules              | Tuples            |
| Dynamic sized types         | Namespaces           | Type aliasing     |
| Enums                       | Operator overloading | Type casting      |
| Error handling              | **Ownership**        | Type inference    |
| Foreign function interfaces | Pattern matching     | Unsafe operations |
| Functions                   | Primitives           | Variables         |
| Generics                    | Raw pointers         | Vectors           |

: A subset of Rust's features, listed in alphabetic order. Key features are marked in bold. {#tbl:rust short-caption="A subset of Rust's features"}





TOKENS

The first stage splits source code into a stream of token trees with a lexer. Tokens can be thought of as words composed by a sequence of characters, e.g., an identifier `foo`, or the keyword `while`. Paired parentheses, braces, and brackets form token trees. Generally, lexers are defined by a regular expression and transformed into a deterministic finite automaton (DFA). Rust's lexer is instead hand-written. The lexer verifies that the code is lexically correct. Every character must map to a token. For instance, compilation may fail if the code contains a non-ASCII character. Non-ASCII characters in identifiers can be enabled with the `#![feature(non_ascii_idents)]`. Albeit, this can be dangerous since it enables homoglyph-based attacks, where two characters visually appear to be the same but have different character codes.

PARSING

: The output of the lexer is sent to a parser which groups tokens together to form an abstract syntax tree (AST). This can be likened to how words form sentences.  


REGION

: The concept of lifetimes originates from region-based memory management. A region is an area of memory allocated for multiple values in a specific step of computation [@Dragon, p.463]. The region can be freed collectively once the computation completes rather than having to free each value separately.

https://internals.rust-lang.org/t/possible-alternative-compiler-backend-cretonne/4275/41
https://users.rust-lang.org/t/proposal-rllvm-rust-implementation-of-llvm-aka-just-use-cretonne/16021/1

https://blog.rust-lang.org/2016/04/19/MIR.html

https://github.com/Cretonne/cretonne


http://www.codecommit.com/blog/scala/universal-type-inference-is-a-bad-thing




Since Scala will not infer the lifetimes of parameters, there is not much point in turning lifetimes are not supported, there is not much point  All types are invariant in the current version. For example, the binary operator `+`


```
```




## Type Classes

Some nodes share similar characteristics. For example, the `File` node takes a sequence of `Items` and



## Constructs

All constructs are implemented as case classes. At the top-level,

## Expressions

Expressions are the easiest construct to implement.
```

```








The DSL is implemented in Scala with dependencies to the Shapeless library. Nodes in the AST are tuple structs. Elements of the tuple structs are treated as child nodes.

```Scala
object Eval {
  trait Eval[A] {
    def eval(x: A): String
  }
  def apply[A](implicit x: Eval[A]): Eval[A] = x
  implicit class EvalOps[A](a: A)(implicit ev: Eval[A]) {
    def eval = Eval[A].eval(a)
  }
}
```

```Scala
trait Exp
trait Lit extends Exp
trait BinOp extends Exp

case class Num(v: String) extends Lit
case class Add[A <: Exp, B <: Exp](l: A, r: B) extends BinOp
```

```Scala
implicit def EvalNum: Eval[Num] =
  x => s"$x"

implicit def EvalAdd[A <: Exp, B <: Exp]
  (implicit l: Eval[A], r: Eval[B]): Eval[Add[A, B]] =
  x => x.l.eval ++ "+" ++ x.r.eval
```


Expression precedence


The Cake Pattern

- Access implicits of subtype

Type Aliases

Having to specify an HList whenever an item is used is both verbose and dangerous. Errors might occur if an HList of incorrect length is specified. Therefore, it would be good to restrict the number of generics, and omit having to specify the HList itself, using a type alias. The following could be a sound approach.

```Scala
trait Foo[T]; object Foo {
  type T[X] = Foo[X::HNil]
}
trait Bar[T]; object Bar {
  type T[X,Y] = Bar[X::Y::HNil]
}
trait Qux[T]; object Qux {
  type T[X,Y,Z] = Qux[X::Y::Z::HNil]
}
```

Type aliases might appear to just be symbolic, but are in fact new types. Problems can arise when trying to unify a type alias which has the same kind as its actual type. For example:

```Scala
implicit def show[F[_],L<:HList]: Show[F[L]] = () => "OK"

implicitly[Show[Foo[i32::HNil]]]                // OK
implicitly[Show[Bar[i32::i32::HNil]]]           // OK
implicitly[Show[Qux[i32::i32::i32::HNil]]]      // OK
implicitly[Show[Foo.T[i32]]]                    // ERROR! Implicit not found
implicitly[Show[Bar.T[i32,i32]]]                // OK
implicitly[Show[Qux.T[i32,i32,i32]]]            // OK
```

Here, `Foo.T[X]` has the same kind as its actual type `Foo[T]`. Because of this, `Foo.T[X]` does not dealias to `Foo[X::HNil]`. Consequently, `Show[Foo.T[i32]]` fails to unify with `Show[F[L]]` with the bound `L<:HList` because `i32` is not an `HList`. Meanwhile, `Bar.T[X,Y]` is dealiased to `Bar[X::Y::HNil]` and successfully unified because its kind differs from its actual type `Bar`. One way of solving this problem is by using type projection.

```
trait Foo {
  trait Foo[T]
  type T = Foo[X::HNil]
}
trait Bar {
  trait Bar[T]
  type T[X,Y] = Bar[X::Y::HNil]
}
trait Qux {
  trait Qux[T]
  type T[X,Y] = Qux[X::Y::Z::HNil]
}
implicitly[Show[Foo#T[Int]]]                // OK
implicitly[Show[Bar#T[Int,Int]]]            // OK
implicitly[Show[Qux#T[Int,Int,Int]]]        // OK
```

In this case, `Foo[Int]#T` i


For example, the binary operator `Add` in [@lst:impl3] requires its left- and right-operand to be of the same type. In consequence, attempting to add `Lit[i32]` with `Lit[f32]` fails with a type mismatch error. `Exp` would need to be covariant over `T` in order to support coercions, but this might cause issues. If `Exp[T]` was `Exp[+T]`, then adding `Lit[i32]` with `Lit[f32]` would succeed and result in `Add[Any]`. Hence, implicit evidence would be needed to ensure that `T` is not inferred to `Any`. `Exp` is invariant over `T` because the DSL does not aim to support lifetime coercions at the moment.

```Scala
trait A[T]
trait B[+T]
trait C[-T]

trait X
trait Y extends X

implicitly[A[X] <:< A[X]] // OK
implicitly[A[Y] <:< A[X]] // FAIL
implicitly[A[X] <:< A[Y]] // FAIL

implicitly[B[X] <:< B[X]] // OK
implicitly[B[Y] <:< B[X]] // FAIL
implicitly[B[X] <:< B[Y]] // OK

implicitly[C[X] <:< C[X]] // OK
implicitly[C[Y] <:< C[X]] // FAIL
implicitly[C[X] <:< C[Y]] // OK
```

### Free Monads

### Lightweight Modular Staging

## Libraries

* LMS and Delite
* Typelevel Scala
* Catz
* Fastparse
* Shapeless
* DSL.scala
* Scala C
* Scala Macros
* Squid
* ScalaMeta
*

## Functional Scala

Scala has a flexible type system.
Miles Sabin, founder of the Shapeless library, points out in a blog post the flexibility of Scala's type system  in his blog post how Scala's type system is isomorphic to a gives a demonstration of how Scala's typesystem is

Curry-Howard points out that the relationship between types and values in a typesystem is isomorphic to the relationships between proofs and propositions in a logic system.
A type system consists of values and
A type system can be viewed as a logical system of

Scala is strongly typed. Types can be annotated for variables, values, parameter lists and return values. Scala has no primitive types, meaning every type instantiates to an object.
All values in Scala have a type. Types come in different forms.
Classes, methods, and traits may take a list of type parameters, i.e., generics. Type parameters may either be specified Type parameters are

Companion objects are singleton objects with the same name as a class.

The compiler follows a specific order when searching for implicits. The first place to look is
 When an object is declared with the same name as a class, it becomes a companion object to that class. Companion objects






The DSL is best evaluated by demonstrating its features in an example program. The program in listing X serves as a template.

```
#[derive(Debug)]
struct Foo<X> {
  x: X,
  y: i32
}
trait Bar<X> {
  fn update(&mut self, x: X, y: i32) {
    self.x += x;
    self.y += y;
  }
}
impl<X> Bar<X> for Foo<X> {
  fn foo
}
impl<X> Foo<X> {
  fn new()
}
```


## Other approaches

The preferable embedding with regards to ease of use is Rust quasiquotes. Rust quasiquotes require a lexer and parser for the Rust language. Writing these from scratch is too big of a task. Generating them is a better option, but there is no canonical Rust grammar targeting Scala available at the time being. Another option is to write code in string literals with language injection. Language injection for Rust using the Rust-IntelliJ plugin only performs static checks up until name resolution. Relying on language injection would also constrain development to the IntelliJ editor.

The DSL statically typed, meaning all types must be known at compile time. This can be problematic since some types depend on information from the front-end IR which is obtained at runtime. To alleviate this, the DSL is able to generate generic code. Moreover, the logic of the generated code can depend on runtime information.

Symbols can be overridden at runtime if desired, breaking type safety. An integer may for example appear as an integer to the type checker, but can be overridden to generate to a float. This does however not implicate all integers must be generated to floats.

Even if the primary aim is to generate code, it could be of interest to add other interpretations in the future, e.g., debugging, transformations, or even code generation to other languages.


 Borrow checking and lifetimes. Enforcing these at compile time would require changing Scala's semantics through macros.

 Implement `PrintWriter` for `show`



### Monads

```{#lst:scala9 .scala caption="Monad, Functor"}
trait Monoid[T] {
  def append(a: T, b: T): T
  def identity: T
}
trait Functor[F[_]] {
  def map[A,B](a: F[A])(fn: A => B): F[B]
}
trait Monad[M[_]] {
  def pure[A](a: A): M[A]
  def flatMap[A,B](a: M[A])(fn: A => M[B]): M[B]
}
```

<!--Tradeoffs--> Deep embeddings are flexible in adding new interpretations, but inflexible in adding new constructs. Adding a new construct requires revising all interpretations. It is the opposite for shallow embeddings where the semantic domain is fixed. There is only one interpretation, and changing it necessitates updating all constructs.

## The Expression Problem

The tradeoff between shallow and deep embeddings is recognized as the *expression problem* [@FoldingDSL]. When implementing an embedded DSL, one can modularize by either constructs or interpretations. The expression problem is best explained with an example [@ExpressionProblem].

Consider a language with two constructs: `Add` and `Num`, and two interpretations: `Eval` and `Emit`. Both `Add` and `Num` are expressions. `Num` represents a number literal. `Add` takes two expressions and adds them together. `Eval` evaluates an AST of `Add` and `Num` constructs into a numerical value, whereas `Emit` generates a string representing the arithmetic expression.

There are two general approaches to implementing this language: an object oriented approach and a functional approach, see Fig {@fig:ExpressionProblem}. The object oriented approach is to modularize by constructs. `Add` and `Num` are classes which both implement `Eval` and `Emit` as methods. The functional approach is to modularize by interpretations. `Eval` and `Emit` are functions which both implement how to handle arguments of type `Add` and `Num`. Both approaches are imperfect. In the former, new constructs can be added by creating a new module, but adding a new interpretations requires changing all existing modules. The opposite applies for the latter.

![Expression problem: Object oriented approach (left) and functional approach (right)](ExpressionProblem.png){#fig:ExpressionProblem short-caption="Expression problem"}

<!--[@FinallyTagless]-->

<!--Another approach is the Finally Tagless technique which builds on *typeclasses*. It uses an interface to abstract over all interpretations. The interface can be implemented to create different concrete interpretations. New constructs and interpretations can be added with ease. Constructs are added by supplementing the interface with new methods. Interpretations are added by creating new instances of the interface.-->

<!--CONVERT ABSTRACT SYNTAX TO CONCRETE SYNTAX-->
<!--https://en.wikipedia.org/wiki/Abstract_syntax-->
<!--https://en.wikipedia.org/wiki/Higher-order_abstract_syntax-->
<!--https://en.wikipedia.org/wiki/Name_binding-->

 A comparison, highlighting one advantage of non-lexical lifetimes can be viewed in Listings X and Y.-->

<!--Both a and b's lifetimes are tied to the scope of main. Since their lifetimes overlap, a.push(9) results in an error. Meanwhile in [@lst:rust9], b's lifetime is non-lexical and thereby not tied to a scope.no error occurs since b's b's lifetime ends earlier, i.e., when it will no longer be used.-->

<!--```{#lst:rust8 .Rust caption="Lexical lifetimes."}-->
<!--fn main() {           //  'a   -->
  <!--let mut a = vec![]; // <-+-->
  <!--a.push(1);          //   |-->
  <!--a.push(2);          //   |'b-->
  <!--let b = &a;         // <-+-+-->
  <!--foo(a);             //   | |-->
  <!--a.push(9);          //   | |-->
<!--}                     // <-+-+-->
<!--```-->
<!--```{#lst:rust9 .Rust caption="Non-lexical lifetimes."}-->
<!--fn main() {           //  'a-->
  <!--let mut a = vec![]; // <-+-->
  <!--a.push(1);          //   |-->
  <!--a.push(2);          //   |'b-->
  <!--let b = &a;         // <-+-+-->
  <!--foo(b);             // <-+-+-->
  <!--a.push(9); // OK    //   |-->
<!--}                     // <-+-->
<!--```-->


Classes and methods can take multiple lists of parameters. If a method is called without specifying all parameter lists, the result is a function taking the remaining lists. This technique is referred to as currying. For classes and methods taking no parameters at all, no parameter list needs to be specified.

```{#lst:scala3 .scala caption="Currying."}
def product(s: Int)(r: Int): Int = l * r
val x = product(3)
val y = x(5)
```


