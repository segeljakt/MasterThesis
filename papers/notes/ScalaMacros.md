# Scala Macros: Let Our Powers Combine!

Keywords: Scala, Macros, DSL, Annotations, Code Generation

Scala version 2.10 introduced macros. Macros enable compile-time metaprogramming. They have many uses, including boilerplate code generation, language virtualization and programming of embedded DSLs. Macros in Scala are invoked during compilation and are provided with a context of the program. The context can be accessed with an API, providing methods for parsing, type-checking and error reporting. This enables macros to generate context-dependent code. Scala provides multiple types of macros: def macros, dynamic macros, string interpolation macros, implicit macros, type macros and macro annotations.

* Def macros generate the body of a function.
* Dynamic macros define a Dynamic trait which a class can implement to generate code based on strings.
* String interpolation macros define rules to desugar string literals. As an example, one can define a rule which reverses a string literal.
* Implicit macros define implicit parameters which are generated automatically by the compiler.
* Type macros are used to generate the body of a type.
* Macro annotations make it possible to generate code by annotation.


String literals in Scala can be written either as single-line or multi-line [LearningScala, ProgrammingScala]. The latter is effective for language embedding, as one can then write code on multiple lines inside of a string literal.

Another feature is string interpolation which lets the user annotate that a set of rules should be applied to a string literal. In Fig X, the `s` string interpolation method inserts an external value into the string. String interpolation desugars the string literal into a `StringContext`, and invokes the string interpolation method on it. External values and variables are passed as arguments to the method call. Custom string interpolation methods can be injected into the `StringContext` class through implicit classes.
[@https://docs.scala-lang.org/overviews/quasiquotes/intro.html]

```{.scala caption="String interpolation"}
val x = "world"
val y = s"Hello $x!"
println(y) // Hello world!
```

```{.scala caption="Desugaring"}
val x = "world"
val y = new StringContext("Hello ", "!").s(x)
println(y) // Hello world!
```

Scala has a set of advanced string interpolation methods for parsing Scala code, referred to as quasi-quotes. Quasi-quotes take a string literal containing code and generates a corresponding abstract syntax tree. Scala's standard library only has quasi-quotes for Scala code. To add support for Rust quasi-quotes, one would need to construct a lexer and parser for Rust.
