\clearpage

# Design

Two approaches were explored for embedding Rust as a DSL in Scala.

## Shallow Embedded DSL

The first DSL is a basic string interpolator which splices snippets of Rust code together. Snippets are statically checked by passing them to the rustc compiler or the syn parser, depending on what is specified by the user. The rustc compiler can only check snippets which represent whole compilation units. In contrast, the syn parser can check individual constructs, but only syntactically. Both a runtime and compile time version of each string interpolator was implemented. The compile time version can only splice snippets in the form of literals, known at compile time.

## Deeply Embedded DSL

The second DSL is statically typed with a deep embedding. It is designed to be extensible in the direction of defining custom structs, functions, and traits. Meanwhile, the implementation of constructs and interpretations is abstracted away from the user. The DSL's API is a combination of fluent interfaces for defining items, and parameterized ADTs for constructing an AST. The Shapeless library is used to make the fluent-interfaces polymorphic and type safe. By building an AST, the DSL has a deep embedding which supports multiple interpretations. The only interpretation available at the time being is Rust code generation.

### Overview

[@Lst:design1] illustrates the directory-structure of the deeply embedded DSL. There are three modules: constructs, types, and interpretations.

```{#lst:design1 caption="Directory tree of the project."}
Rust-DSL/
+-AST.scala
+-Constructs/
| +-File.scala
| +-Expressions/
| | +-Literal.scala
| | +-Operators.scala
| | +-ControlFlow.scala
| | +-Let.scala
| +-Items/
| | +-Function.scala
| | +-Struct.scala
| | +-Trait.scala
| | +-Impl.scala
| +-Verbatim.scala
+-Types.scala
+-Interpretations/
  +-Showable.scala
```

AST defines the node and interpretation categories of the DSL. Constructs define parameterized ADT-encoded nodes for building the AST. File is the root-node of the AST. Expressions contain nodes for representing literals, control-flow expressions, operators, and let-declarations. Items provide fluent interfaces for building functions, structs, traits, and trait implementations. The main missing constructs are enums, tuples, arrays, macros, type aliases, and patterns. At the moment, these can be written in Verbatim if necessary. Types encompass Rust's primitive, reference, and nominal types. It also contains utilities for guiding Scala's type inference. Interpretations provide a `Showable`{.Scala} interpretation which nodes extend to generate code. Types in contrast use a type class for generating code. A program is synthesized by traversing its AST in top-down order. By default, types in the generated code have the same identifier as in the DSL. Identifiers for functions, variables, fields, and arguments, are specified by the user.

## Emulating `rustc`

The DSL attempts to issue a subset of `rustc`'s static checks through `scalac`. Table X lists supported and non-supported features.

| Static checks       | Supported |
|---------------------+-----------|
| Syntactic checks    | Yes       |
| Macro expansion     | No        |
| Name resolution     | Yes       |
| Type inference      | Yes       |
| Type checking       | Yes       |
| Lifetime elision    | No        |
| Region inference    | No        |
| Trait resolution    | Yes       |
| Method lookup       | Yes       |
| Coersions           | No        |
| Mutability checking | No        |
| Borrow checking     | No        |

: Static checks. [@RustOperators]. {#tbl:ops}

Parameterized ADTs ensure grammatical correctness since each node requires its children to be a specific syntactic unit. For example, a binary addition operator requires its two operands to be expressions, and will reject anything else. GADTs also establish type safety by enforcing both operands to be parameterized by the same type. If a type parameter is unspecified, Scala will attempt to infer it. Programs are written in higher-order abstract syntax for automatic name resolution. In other words, when a node refers to a variable, it will refer directly to the node of the variable rather than its identifier. Traits in Rust are programmed as type classes in Scala. Thereby, trait resolution piggybacks on implicit resolution. The main unsupported semantics are mutability checks, coercions, lifetime inference and borrow checking.
