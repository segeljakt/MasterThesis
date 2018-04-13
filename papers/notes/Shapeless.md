# Shapeless

Native Scala does not support first-class polymorphic function values [@http://milessabin.com/blog/2012/04/27/shapeless-polymorphic-function-values-1/]. Function parameters can be polymorphic with respect to either arity or types. Hence, a Scala function can either take a fixed set of first-class generic type parameters (listing [@X]), or a list of parameters with a second-class generic type bound (listing [@Y]).

```{.scala caption="A function which takes a fixed set of first-class generic type parameters."}
def f[A,B,C](a: A, b: B, c: C)
```

```{.scala caption="A function which takes a list of parameters with a second-class generic type bound."}
def f[T](l: List[T])
```

Shapeless is a library for generic programming which can be used to solve this tradeoff [@https://www.scala-exercises.org/shapeless/polymorphic_function_values, @shapeless-guide.pdf]. The core feature of Shapeless is heterogeneous lists (HLists). A HList is a List where the type of each element is statically known at compile time [@https://jto.github.io/articles/getting-started-with-shapeless/]. Compared to tuples, HLists are able to abstract over arity and support Scala's List operations, e.g., map and zip. Functions which accept HLists as parameters retain both arity and type information, and thereby become polymorphic.


```{.scala caption=""}

// An HList
val x = 3 :: "abc" :: 'o' :: HNil

// A polymorphic function
def f[T <: HList](l: T)

// A polymorphic function, where parameters
// must be subtypes of MyType
def f[T <: HList : <<:[MyType]#λ](l: T)
```

# Polymorphic function values

```
object X extends (Set ~> Option) {
  def apply[T](s: Set[T) = s.headOption
}

object Y extends Poly1 {
  implicit def caseInt = at[Int](x => 1)
}
```

# HLists

```
val s = Set(1) :: Set("foo") :: HNil
```

# Covariant

```
trait X
case class XA() extends X
case class XB() extends X
```

# HMap

```
class BiMapIS[K, V]
implicit val intToString = new BiMapIS[Int, String]
implicit val stringToInt = new BiMapIS[String, Int]

val hm = HMap[BiMapIS](23 -> "foo", "bar" -> 13)
```

# Records

```
val book =
  ("name" ->> "Foo") ::
  ("age"  ->> "20") ::
  HNil

scala> book("author")  // Note result type ...
res0: String = Foo
```

# Coproducts

```
type X = Int :+: Char :+: CNil

val x = Coproduct[X]("Foo")
isb.select[String]
```

# Generic representation of case classes

## Generic[T]: Convert to and from case class T
```
case class Foo(i: Int, c: Char)

val foo = Foo(1, 'c')

val fooHList = Generic[Foo].to(foo) // Convert from Foo to HList

val foo2 = Generic[Foo].from(fooHList) // Convert from HList to Foo
```

## FnToProduct[T]: Convert 

# Lenses

```
case class Book(name: String, age: Int)

val nameLens = lens[Foo] >> 'name
val ageLens = lens[Foo] >> 'age

val book = Book("foo", 3)
val age1 = ageLens.get(book)
```

# Auto type class instance derivation

```
import MonoidSyntax._
import Monoid.auto._

case class X(i: Int, s: String)

Foo(1, "a") |+| Foo(5, "9")
// Foo(2, "a9")
```

---------------------------------------------------------------------------

The Aux pattern

```
trait Last[A] {
  type B
  def last(a: A): B
}

object Last {
  type Aux[A,B0] = Last[A] { type B = B0 }

  implicit def tuple1Last[A]: Aux[Tuple1[A],A] = new Last[Tuple1[A]] {
    type B = A
    def last(a: Tuple1[A]) = a._1
  }

  implicit def tuple2Last[A,C]: Aux[(A,C),C] = new Last[(A,C)] {
    type B = C
    def last(a: (A,C)) = a._2
  }
}

def sort[A,B](l: List[A])(implicit last: Last.Aux[A,B], ord: Ordering[B]) = ???
```
