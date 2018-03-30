# Report for Week 11

## What went right

* Refactored the Problem section, fixed citations, added listings, and wrote about the Shapeless library.
* Reimplemented some DSL constructs to fully support typechecking.
* Created tests for evaluating the typechecking capabilities of the AST. Users can for example declare functions and make function calls in the AST with typechecking on the input and output.

## What went wrong

* Did not integrate work with Oscar, since we could not meet in person (Vacation)
* Encountered two bugs, one in Scala and one in Shapeless. The Shapeless bug might prevent users from declaring Rust functions with generic parameters. The Scala bug makes it more difficult to support tuples.

## Plan for next week

* Add information to the Rust background about the Rust compiler and write in the design about how the DSL handles static checking.
* Figure out if it is possible to support static checking for pattern matching in the DSL.
