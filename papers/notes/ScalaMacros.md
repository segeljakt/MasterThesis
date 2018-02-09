# Scala Macros: Let Our Powers Combine!

Keywords: Scala, Macros, DSL, Annotations, Code Generation

Scala version 2.10 introduced macros. Macros enable compile-time metaprogramming. They have many uses, including boilerplate code generation, language virtualization and programming of embedded DSLs. Macros in Scala are invoked during compilation and are provided with a context of the program. The context can be accessed with an API, providing methods for parsing, type-checking and error reporting. This enables macros to generate context-dependent code. Scala provides multiple types of macros: def macros, dynamic macros, string interpolation macros, implicit macros, type macros and macro annotations.

* Def macros generate the body of a function.
* Dynamic macros define a Dynamic trait which a class can implement to generate code based on strings.
* String interpolation macros define rules to desugar string literals. As an example, one can define a rule which reverses a string literal.
* Implicit macros define implicit parameters which are generated automatically by the compiler.
* Type macros are used to generate the body of a type.
* Macro annotations make it possible to generate code by annotation.
