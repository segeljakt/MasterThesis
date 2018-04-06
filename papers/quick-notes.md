# Grammars

https://github.com/bleibig/rust-grammar
https://github.com/jorendorff/rust-grammar
https://github.com/nikomatsakis/rustypop
https://github.com/jbclements/rust-antlr/
https://github.com/willy610/bnf2railroad
https://github.com/harpocrates/language-rust/tree/master/src/Language/Rust/Parser
https://github.com/rust-lang/rust/tree/master/src/grammar

# Upcoming Rust features

NLL
SIMD
const generics
const fn
encoding type movability
generators
procedural macros

# Lost Rust features

https://github.com/rust-lang/rust/issues/4632

* Typestate system https://github.com/rust-lang/rust/commit/41a21f053ced3df8fe9acc66cb30fb6005339b3e
* Effect system
* Function complexities: parameter modes, argument binding, stack iterators, tail calls
* Language-integrated runtime for tasks, channels, logging
* GC pointers, task local GC (yes, rustboot had a real mark/sweep) https://github.com/rust-lang/rust/pull/18967
* Dynamic, structural object types

# AST
* Concrete Syntax Tree: Concrete identifiers + Concrete structure (Parse Tree)
* Abstract Syntax Tree: Concrete identifiers + Abstract structure (Requires name resolution)
* Higher Order Abstract Syntax Tree: Abstract identifiers + Abstract structure

* Abstract Semantic Graph

http://www.lix.polytechnique.fr/Labo/Dale.Miller/papers/slp87.pdf
https://en.wikipedia.org/wiki/Abstract_syntax

# RFCs

https://docs.google.com/spreadsheets/d/18FtPnEI8RTPdFhGDtj21ITndakEtxVrs8pVzRNx-jL8/edit#gid=0
./configure

# TypeClasses
http://like-a-boss.net/2013/03/29/polymorphism-and-typeclasses-in-scala.html

Subtype polymorphism
Parametric polymorphism
Ad-hoc polymorphism
And that’s what’s really cool about typeclasses. We can add new functionality to old code without modifying it.

# Other

Higher kinded types = Type constructors
Curried type constructors
Kind projector
Type erasure
