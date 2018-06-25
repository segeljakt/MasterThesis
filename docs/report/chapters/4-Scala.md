\clearpage

# The Scala Programming Language

Scala is a high level object-oriented and functional programming language. It combines the core features of Java, e.g., static typing and generic collections, with a concise and elastic syntax. Because Scala is a JVM language, Scala and Java code integrate well with each other.

## Basics

Classes, traits, and objects are the basis of Scala programs [@ScalaReference]. These are containers for members. A member is either a value, variable, method, type member, class, trait, or object. Values and variables store the result of an expression, and are immutable and mutable respectively. Methods are expressions which take parameters as input. Traits can contain abstract members and are equivalent to mixins in other languages. Classes in contrast require their members to be concrete, unless declared abstract. In addition, members can be included from multiple traits but can only be inherited from a single super class. A class or trait can also be declared as sealed, requiring all its subtypes to be declared within its file scope. Objects are singleton instances of classes.

```{#lst:scala1 .scala caption="Class, trait, and val."}
abstract class Animal {
  def sound()
}
trait Fur {
  val color: String
}
class Bear(color: String) extends Animal with Fur {
  def sound() = println("Growl")
}

val grizzly = new Bear("brown")
grizzly.sound() // Growl
```

Case classes are classes whose members are by default immutable and public [@ScalaReference]. Methods, including `toString`, `equals`, `hashCode`, `apply`, and `unapply` are automatically generated for case classes. The `equals` method compares case classes by structure, rather than reference. The `apply` method is a shorthand constructor which can be invoked without needing to specify the `new` keyword. The `unapply` method allows case classes to be deconstructed for pattern matching. To this end, case classes serve as a convenient alternative to ordinary classes.

```{#lst:scala2 .scala caption="Case class."}
case class Snake() extends Animal {
  def sound() = println("Hiss")
}

val cobra = Snake()
cobra.sound()
```

## Implicits

One of Scala's special features are *implicits* [@ScalaReference]. Implicits are either values or methods which may automatically be inserted by the compiler into parts of the program. There are two types of implicits: implicit parameters and implicit conversions.

An implicit parameter is a parameter to a method or class which does not need to be passed explicitly by the user. Instead, the compiler will search for an implicit of the specified type and insert it automatically.

```{#lst:scala4 .scala caption="Implicit parameters."}
implicit val foo: Int = 3
def bar(implicit ev: Int): Int = ev
println(bar) // 3
```

An implicit conversion is when the compiler automatically converts an object into an object of another type. In [@lst:implicitconv], method `m` is invoked on class `C`. Since `C` does not implement `m`, the compiler will search for an implicit conversion from `C` to a class implementing `m`. In this case, `conv` provides an implicit conversion from `C` to `I`. `I` implements `m`, and as a result `o` is converted to an object of class `I`.

```{#lst:implicitconv .scala caption="Implicit conversions"}
class C
class I {
  def m: String = "Hello"
}
implicit def conv(from: C): I = new I
val o: C = new C
o.m // "Hello"
// convertCtoI(o).m
```

Scala offers *implicit classes* which are syntactic sugar for injecting extension methods into classes through implicit conversions. The code in [@lst:implicitclass] injects the extension method `m` into class `C`, and is equivalent to the code in [@lst:implicitconv].

```{#lst:implicitclass .scala caption="Implicit classes"}
class C
implicit class I(o: C) {
  def m: String = "Hello"
}
val o: C = new C
o.m // "Hello"
```

### Type Classes

Scala does not support type classes as first class citizens, but can express them through implicits. [@Lst:typeclass] illustrates `String` and `Int` can be extended with type class `Print` to print values of their type. The trait `Print` is a type class, whereas `printString` and `printInt` are instances of the type class. If a trait or abstract class contains a single abstract method (SAM), instances of it can be written as lambda functions instead of typing out the whole method [@ScalaReference, ch. 6]. The `printInt` is an example of how SAM can be used.

```{#lst:typeclass .scala caption="Type class"}
// Type class
trait Print[T] {
  def print(s: T): Unit
}
// Type class instance
implicit def printString: Print[String] = new Print[String] {
  def print(s: String) = println(s)
}
// Type class instance (SAM)
implicit def printInt: Print[Int] = v => println(v.toString)

def test[T](s: T)(implicit ev: Print[T]) = ev.print(s)
test(3)     // Int: 3
test("foo") // String: foo
```

### Implicit type inference

Another use of implicit parameters is to guide the type inference. For instance, the code in listing [@lst:default] illustrates how implicits can be used to infer a default type for a type parameter `T`. If `T` is unspecified when calling `test`, it will by default be inferred to `Int`. This is accomplished through two implicits. Implicit resolution will find the most specific implicit, which is in this case `foo`. The difference between `foo` and `bar` is how `foo` requires both type parameters of `Default` to be of the same type. Hence implicit resolution for `Default[T,Int]` will resolve to `foo` if `T` is unbound, and thereby unify `T` with `Int`. In contrast, `Default[Double,Int]` will resolve to `bar` since the types don't match.

```{#lst:default .scala caption="Inferring a default type parameter, from [@Default]."}
trait Default[A,B]
object Default {
  implicit def foo[T]   = new Default[T,T] {}
  implicit def bar[T,D] = new Default[T,D] {}
}

def test[T](implicit ev0: Default[T,Int], ev1: ClassTag[T]) = println(ev1)
test         // Int
test[Double] // Double
```

Note how a `ClassTag` is needed for printing the type name. An object's class can be accessed through `myObject.getClass`{.Scala}, but this gives no information about its type parameters. The reason is the JVM erases type parameter information at runtime. Hence, a `List[Int]`{.Scala} and a `List[Double]`{.Scala} both have the same type `List`{.Scala} at runtime. To evade type erasure, one can implicitly request a `ClassTag`{.Scala} or `TypeTag`{.Scala} from the compiler. `ClassTags`{.Scala} solely contain information about a runtime class, i.e., a `ClassTag[List[Int]]`{.Scala} only gives information about `List`{.Scala}. `TypeTags`{.Scala} contain information available at compile time, i.e., `TypeTag[List[Int]]`{.Scala} includes information about both `List`{.Scala} and `Int`{.Scala}.

<!--### Macros-->

<!--Scala version 2.10 introduced macros. Macros enable compile-time metaprogramming. They have many uses, including boilerplate code generation, language virtualization and programming of embedded DSLs. Macros in Scala are invoked during compilation and are provided with a context of the program. The context can be accessed with an API, providing methods for parsing, type-checking and error reporting. This enables macros to generate context-dependent code. Scala provides multiple types of macros: def macros, dynamic macros, string interpolation macros, implicit macros, type macros and macro annotations.-->

## Shapeless

Shapeless is a generic programming library for Scala. Its core abstraction is heterogeneous lists (HLists) [@Shapeless]. An HList is a linked list where each element may have a unique type. Unlike regular Lists, HLists preserve type information of their elements, and unlike Tuples, HLists abstract over arity. [@Lst:hlist] illustrates how tuples cannot be prepended, but preserve type information of their elements. Lists can be prepended, but lose type information since all elements are coerced into the most common supertype. HLists can be prepended, while preserving type information, and as a result overcome the trade off between tuples and lists.

```{#lst:hlist .scala caption="Tuple, List and Shapeless HList."}
> val tuple = (5, "abc", 'o')
tuple: (Int, String, Char) = (5, abc, o)
> val list = List(5, "abc", 'o')
list: List[Any] = List(5, abc, o)
> val hlist = 5 :: "abc" :: 'o' :: HNil
hlist: Int :: String :: Char :: HNil = 5 :: abc :: o :: HNil

// Arity test - Prepend one element
> val tuplePrepend = 4 :: tuple
error: value :: is not a member of (Int, String, Char)
> val listPrepend = 4 :: list
lisAppend: List[Any] = List(4, 5, abc, o)
> val hlistPrepend = 4 :: hlist
hlist: Int :: Int :: String :: Char :: HNil = 4 :: 5 :: abc :: o :: HNil

// Polymorphism test - Extract head integer element
> val tupleHead: Int = tuple._1
tupleHead: Int = 4
> val listHead: Int = list(0)
error: type mismatch, found: Any, required: Int
> val hlistHead: Int = list(0)
hlistHead: Int = 4
```

The `A :: B` syntax is syntactic sugar for `::[A,B]`. An HList is thereby composed of nested type constructors. Similarly to how the last node in a linked list may point to null, the last type constructor is terminated by `HNil`, e.g., `Int::Int::HNil`. Shapeless also provides natural transformations for HLists. It is for example possible to map over the elements of an HList and apply a different function to each element depending on its type, as shown in listing [@lst:poly1].

```{#lst:poly1 .scala caption="Polymorphic function"}
> object toInt extends Poly1 {
    implicit def caseInt = at[Int](x => x)
    implicit def caseString = at[String](x => x.length)
    implicit def caseChar = at[Char](x => 1)
  }

> val hlist2 = hlist map toInt
hlist2: Int :: Int :: Int :: HNil = 5 :: 3 :: 1 :: HNil
```

Operations on HLists are implemented as type-classes. An example is the `Comapped` type class. Shapeless' documentation describes `Comapped` as a *"type class witnessing that the result of stripping type constructor `F` off each element of HList `L` is `Out"`* [@HListOps]. Listing [@lst:comappedexample] shows how `Comapped[A,Option]` witnesses that each element in `A` is wrapped inside an `Option` type constructor. `Comapped.Aux[A,Option,B]` witnesses that the result of stripping the `Option` off each element of `A` is `B`.

```{#lst:comappedexample .scala caption="Comapped example."}
type A = Option[Int] :: Option[Char] :: HNil
type B = Int :: Char :: HNil

implicitly[Comapped[A,Option]]       // OK
implicitly[Comapped.Aux[A,Option,B]] // OK
```

The main parts of the implementation for `Comapped` are displayed in listing [@lst:comapped]. The `Comapped` trait is the type class. It takes HList `L` and type constructor `F` as type parameters and produces a HList `Out` type member. The `Comapped` companion object declares an `Aux` type alias. `Aux` is a common pattern in type level programming for representing type members as type parameters [@Astronaut, ch. 3]. The convention is to unify the result of the type level computation with the last type parameter in `Aux`.

Next are the type class instances `hnilComapped` and `hlistComapped`. The former witnesses that the `Comapped` of an empty HList is `HNil`. The latter witnesses that the result of stripping `F` from the head `F[H]` of `L` and comapping the tail `T` to `TCM` is `H::TCM`. `Poly1` uses the same technique as `Comapped`, but lets the user add custom type class instances for handling elements of specific types.

```{#lst:comapped .scala caption="Comapped implementation, from [@HlistOps]."}
trait Comapped[L <: HList, F[_]] {
  type Out <: HList
}

object Comapped {

  type Aux[L <: HList, F[_], Out0 <: HList] =
    Comapped[L, F] { type Out = Out0 }

  implicit def hlistComapped[H, T <: HList, F[_], TCM <: HList](
    implicit ev: Comapped.Aux[T, F, TCM]
  ): Aux[F[H]::T, F, H::TCM] =
    new Comapped[F[H]::T, F] { type Out = H::TCM }

  implicit def hnilComapped[F[_]]: Comapped[HNil, F] =
    new Comapped[HNil, F] { type Out = HNil }
}
```

Another feature of Shapeless are records [@Shapeless]. A record is an `HList` where each value has an associated key. Keys are encoded as singleton typed literals and can be used to lookup or update values of the record. If a variable has a singleton type, then that variable can only assume one possible value. Singleton types have always been used internally by the Scala compiler, but there has not been syntax to express them externally. Shapeless exposes singleton typed literals to the programmer through implicit macros. In listing [@lst:records], `"foo" ->> 5` creates a value of type `FieldType[String("foo"), Int]`, which is an alias for `Int extends KeyTag[String("foo"), Int]` [@Labelled]. `String("foo")` is a singleton type. The call to `record("foo")` requests implicit evidence for the existence of type `FieldType[String("foo"),V]` in the record, where `V` is unbound. The evidence is as a result the associated value of type `V`.

```{#lst:records .scala caption="Shapeless records."}
> val record = ("foo" ->> 5) :: ("bar" ->> "abc") :: ("qux" ->> 'o') :: HNil

> val value = record("foo")
value: Int = 5
```

Future versions of Scala will bring first-class language support for singleton types, as depicted in listing [@lst:refined] [@SingletonType]. Another library which depends on singleton types is refined [@Refined]. Refined adds refinement types to Scala which are types that constrain their values. For instance `Int Refined Positive` constrains the `Int` to only accept positive values.

```{#lst:refined .scala caption="Refined"}
val five: 5 = 5 // Ok
val four: 4 = 5 // Error

// refined
val foo: Int Refined Positive = 3     // Ok
val bar: Int Refined Positive = -2    // Error
val qux: Int Refined Greater[20] = 22 // Ok
val baz: Int Refined Greater[20] = 10 // Error
```

## Scala DSL patterns

Scala is a language well suited for embedding domain specific languages.

<!--### Dynamically typed DSLs-->

<!--TO BE WRITTEN-->

<!--```-->
<!--object Foo extends Dynamic { ... }-->
<!--Foo.bar                // Foo.selectDynamic("bar")-->
<!--Foo.bar = "baz"        // Foo.updateDynamic("bar")("baz")-->
<!--Foo.bar("baz")         // Foo.applyDynamic("bar")("baz")-->
<!--Foo.bar(baz = "qux")   // Foo.applyDynamicNamed("bar")(("baz" -> "qux"))-->
<!--Foo.bar("baz") = "qux" // Foo.applyDynamic("bar").update("baz", "qux")-->

### Fluent Interfaces and Method Chaining

A fluent interface is a technique for initializing objects through a chain of method calls [@PracticalScalaDSLs, ch. 3]. Each call instantiates a new object which is an updated version of the old object. As an example, [@lst:fluentinterface] highlights a fluent interface '`Animal`' which is initialized and printed through a chain of method calls.

```{#lst:fluentinterface .scala caption="Fluent interface."}
case class Animal(species: String = "", color: String = "") {
  def setSpecies(newSpecies: String) = Animal(newSpecies, color)
  def setColor(newAge: Int)          = Animal(species, newColor)
  def print                          = println(color + " " + species)
}
Animal()
  .setSpecies("Fox")
  .setColor("Gray")
  .print() // Gray Fox
```

Spark's RDD API and Flink's DataStream API are instances of where this technique sees use. Through representing assignments as method calls, the object's fields can be updated without being declared as mutable. By also chaining methods one after the other, the user does not need to declare excess local variables for storing intermediate results.

### String literals

A crude approach to embedding a DSL in Scala is to envelop its code in a string literal ([@lst:literal]). Although this approach is easy to program, Scala is unable to check statically check the embedded code. An example where this pattern sees use it the Open Computing Language (OpenCL), where source code for the GPU can be written inside raw C string literals [@OpenCL]. Scala's string literals can either be written as single-line or multi-line [@ProgrammingScala]. The latter is effective for language embedding because it is a continuous block of text with fewer escape characters, since double-quotes need not be escaped.

```{#lst:literal .scala caption="String literal."}
val snippet =
  """
    object Test {
      println("Hello world")
    }
  """
```

### Language Injection

Language injection is a feature offered by the IntelliJ IDEA editor for metaprogramming [@LanguageInjection]. It can be used to imbue syntax highlighting and linting for a programming language, e.g., HTML, CSS, SQL, RegExp, into a string literal. The feature can either be enabled temporarily through IntelliJ's interface, or by placing comment annotations in the source code as in [@lst:inject]. The IntelliJ-Rust plugin for IntelliJ adds support for Rust Language Injection [@RustIntelliJ], and is capable of statically checking code up until name resolution. It will therefore detect errors such as name clashes but will miss type mismatches and ownership violations.

```{#lst:inject .scala caption="Language Injection."}
// language=Scala
val lintedSnippet =
  """
    object Test {
      println("Hello world")
    }
  """
```

### String Interpolation and Quasi Quotation

String interpolation is a feature in Scala which lets the user annotate that rules should be applied to a string literal [@ProgrammingScala, pp. 153]. In [@lst:scala18], the `s` string interpolator splices, i.e., inserts, an external value into the string literal. Scala desugars the string literal into a `StringContext`, and invokes the string interpolation method on it. External values are passed as arguments to the method call. Custom string interpolation methods can be implemented for the `StringContext` through type classes 

```{#lst:scala18 .scala caption="The 's' string interpolator"}
val x = "world"
s"Hello $x." // Desugars to: new StringContext("Hello ", ".").s(x)
```

Scala has a collection of advanced string interpolators for metaprogramming, referred to as quasi-quotes [@QuasiQuotes]. A quasi-quote takes a string literal containing a snippet of Scala code and constructs a corresponding Scala AST. Quasiquotes may be spliced with type names, term names, modifiers, flags, symbols, ASTs, and more. This facilitates the possibility of merging quasiquotes together to form a larger AST. ASTs can consequently be compiled down to byte code with runtime reflection, or deconstructed with pattern matching. Spark SQL's Catalyst optimizer relies on quasiquotes for translating incoming SQL queries into Scala byte code [@SparkSQL]. Scala's native quasi quotes are not type safe, and are limited to only supporting Scala code. Support for another language could be added by writing or generating a lexer and parser. Alternatively, Squid is an open source project for creating custom type safe Scala quasi-quote DSLs [@Squid].

```{#lst:scala19 .scala caption="Scala quasi-quotes"}
val abstractSyntaxTree =
  q"""
    object Test {
      println("Hello world")
    }
  """
```

### Algebraic Data Types

Algebraic Data Types (ADTs) are a category of recursive data types with algebraic properties [@ProgrammingScala, ch. 16]. Algebras are defined by objects, operations, and laws. Objects can for example be numbers or booleans. Operations combine objects into new ones. Laws dictate the relationships between objects and operations, e.g., associative, commutative, and distributive laws. As an example, a linked list is an ADT. Scala's `List` has two subtypes: `Nil` and `::`. The former is the empty `List`, and the latter combines an element with a `List` to form a new `List`. The number of values a `List` can attain is thus confined, and make it possible to reason clearly about the `List`'s behavior. ADTs are suited for embedding DSLs in abstract syntax. For example, [@lst:ADT] defines an algebra of integer literals and addition which is subsequently interpreted with `eval`.

```{#lst:ADT .scala caption="ADT example [@GADT]."}
sealed trait Exp
case class Lit(v: Int)         extends Exp
case class Add(l: Exp, r: Exp) extends Exp

def eval(exp: Exp): Int = exp match {
  case Lit(v)   => v
  case Add(l,r) => eval(l)+eval(r)
}

eval(Add(Lit(1), Add(Lit(9), Lit(3)))) // 13
```

### Generalized Algebraic Data Types

An issue with the ADT in [@lst:ADT] is the expectation that every expression evaluates to `Int`. This prevents the ADT from being extended with new types of algebra, e.g., `Boolean` algebra. One solution is to couple each expression with some form of evidence, i.e., type tag, indicating what it evaluates to. This kind of encoding type checks at runtime, and is known as a *tagged* encoding [@FinallyTagless]. Generalized Abstract Data Types (GADTs) generalize parameterized ADTs over the types expressions evaluate to [@PADT]. Hence, GADTs have *tagless* encoding since no type tags are involved. The DSL in [@lst:GADT] displays how [@lst:ADT] can be generalized and extended with the `Eq` operator of `Boolean` algebra.

```{#lst:GADT .scala caption="GADT DSL example [@GADT]"}
sealed trait Exp[T]
case class Lit[T](v: T)                  extends Exp[T]
case class Add(l: Exp[Int], r: Exp[Int]) extends Exp[Int]
case class Eq(l: Exp[Int], r: Exp[Int])  extends Exp[Boolean]

def eval[T](exp: Exp[T]): T = exp match {
  case Lit(v)    => v
  case Add(l, r) => eval(l) + eval(r)
  case Eq(l, r)  => eval(l) == eval(r)
}
eval(Eq(Add(Lit(1), Lit(2)), Lit(3))) // true
```

### Tagless Final

ADTs and GADTs both suffer from the expression problem [@FinallyTagless]. The expression problem is defined as:

> "The expression problem is a new name for an old problem. The goal is to define a datatype by cases, where one can add new cases to the datatype and new functions over the datatype, without recompiling existing code, and while retaining static type safety (e.g., no casts)."
>
> - Philip Wadler [@ExpressionProblemQuote]

[@Lst:ADT] and [@lst:GADT] modularize by interpretations and as a result are difficult to extend with new algebras. Tagless Final is an embedding which solves the expression problem [@FinallyTagless]. While ADTs and GADTs build a tree of values, Tagless Final instead forms a tree of function calls. The former two have an *initial* encoding, and Tagless Final, as the name denotes, has a *final* encoding. Tagless Final solves the expression problem through type classes. [@Lst:tagless] re-implements the ADT in [@lst:ADT] with a tagless final encoding. `Lit` and `Add` are type classes defining the syntax of the language. The `evalLit`, `evalAdd`, `showLit` and `showAdd` implicits are type class instances defining the DSL's semantics. The first two evaluate to an `Int`, and the second two concatenate a `String`. The `test` method implicitly imports the semantics into scope. By de-coupling constructs from interpretations, the program becomes easier to extend. Creating a new construct involves defining a new type class, along with type class instances for it covering all interpretations. Creating a new interpretation is the opposite.

```{#lst:tagless .scala caption="Tagless Final example [@TaglessFinalScala]"}
trait Lit[T] { def apply(i: Int): T     }
trait Add[T] { def apply(l: T, r: T): T }

implicit val evalAdd: Add[Int] = (l, r) => l + r
implicit val evalLit: Lit[Int] = i => i

implicit val showAdd: Add[String] = (l, r) => s"($l + $r)"
implicit val showLit: Lit[String] = i => i.toString

def test[T](implicit add: Add[T], lit: Lit[T]): T =
  add(add(lit(1), lit(9)), lit(5))

test[Int]    // 15
test[String] // ((1 + 9) + 5)
```

<!--### Free Monads-->

### Lightweight Modular Staging

Lightweight Modular Staging (LMS) is a generative programming approach which unifies the logic of the object language with the logic of the meta language.

```{#lst:lms .scala caption="Lightweight Modular Staging example [@LMS]"}
trait Base {
  type Rep[+T]
}

trait Algebra extends Base {
  implicit def lit(v: Int):          Rep[Int]
  def add(l: Rep[Int], r: Rep[Int]): Rep[Int]
}

trait Show extends Base {
  type Rep[+T] = String
}

trait ShowAlgebra extends Algebra with Show {
  implicit def lit(v: Int):          Rep[Int] = v.toString
  def add(l: Rep[Int], r: Rep[Int]): Rep[Int] = s"$l+$r"
}

trait Test { this: Algebra =>
  def test(x: Int, y: Rep[Int]): Rep[Int] = add(add(x, y), y)
}

new Test with ShowAlgebra {
  println(test(20,"(5+1)")) // "20+(5+1)"
}
```
