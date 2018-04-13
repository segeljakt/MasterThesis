Can your application be structured in such a way that both the data model and the set of virtual operations over it can be extended without the need to modify existing code, without the need for code repetition and without runtime type errors.”

As the above formulation indicates, the expression problem is about two di- mensional extensibility

http://www.scala-lang.org/docu/files/TheExpressionProblem.pdf






Curry-Howard tells us that the relationships between types in a type system can be viewed as an image of the relationships between propositions in a logical system (and vice versa)

A => Nothing

On the logical side of Curry-Howard this maps to A ⇒ false, which is equivalent to ¬A. This seems fairly intuitively reasonable — there are no values of type Nothing, so the signature A => Nothing can’t be implemented (other than by throwing an exception, which isn’t allowed).

http://milessabin.com/blog/2011/06/09/scala-union-types-curry-howard/





