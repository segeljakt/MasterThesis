
# Basics

**Classes, traits, objects.**

Classes, traits, and objects may contain members. Members can be inherited by a class or trait from a single super class, and included from multiple traits. Traits are equivalent to mixins in other languages. A member can either be a value, variable, method, type alias, class, trait, or object.

**Values, variables, methods, and type aliases**

Values and variables store the result of an expression, and are immutable and mutable respectively. Methods are expressions which take parameters. 

```Scala
abstract class Animal {
  def sound()
}
trait Fur {
  val color: String
}
class Bear(color: String) extends Animal with Fur {
  def sound() = println("Growl")
}

val bear = new Bear("gray")
bear.sound()
```

**Abstract and Sealed classes**
The result of instantiating a class is an object. When a class is declared abstract, it cannot be instantiated but can contain undefined members. A class or trait declared as sealed requires all its subtypes to be declared within its file scope.



**Case classes**
Case classes are classes whose members are by default immutable and public. Methods, including toString, equals, hashCode, apply, and unapply are automatically generated for case classes. The equals method compares case classes by structure, rather than reference. The apply method is a shorthand constructor which can be invoked without needing to specify the new keyword. The unapply method allows case classes to be deconstructed for pattern matching. To this end, case classes serve as a convenient alternative to regular classes.

```Scala
case class Snake() extends Animal {
  def sound() = println("Hiss")
}

val snake = Snake()
```

Classes and methods can take multiple lists of parameters. If a method is called without specifying all parameter lists, the result is a function taking the remaining lists. This technique is referred to as currying. In addition as syntactic sugar, the empty list does not have to be specified for parameterless classes and methods.

```
def product(s: Int)(r: Int): Int = l * r
val x = product(3)
val y = x(5)

def hello = println("hello")
hello
```


## Type system

All values in Scala have a type. Types come in different forms.
Classes, methods, and traits may take a list of type parameters, i.e., generics. Type parameters may either be specified Type parameters are

Companion objects are singleton objects with the same name as a class.



Functors - function constructors
Kind





```def foo[T : Bar] => def foo[T](implicit ev: Bar[T])```
https://github.com/non/kind-projector
