```

|-----------------------------------------------------------------------------------+-------|
| Title                                                                             | Cited |
|-----------------------------------------------------------------------------------+-------|
| Shallow Embedding of DSLs via Online Partial Evaluation                           |       |
| Apache Flink: Stream and Batch Processing in a Single Engine                      |       |
| The Rust Programming Language 2                                                   |       |
| General purpose languages should be metalanguages                                 |       |
| Scala-virtualized                                                                 |       |
| Ziria: A DSL for wireless systems programming                                     |       |
|-----------------------------------------------------------------------------------+-------|
| Polymorphic embedding of DSLs                                                     |       |
| Concealing the deep embedding of DSLs                                             |       |
| A Fast Abstract Syntax Tree Interpreter for R                                     |       |
| Code Generation with Templates                                                    |       |
| A Python Wrapper Code Generator for Dynamic Libraries                             |       |
| The Scala Programming Language                                                    |       |
| Data stream processing via code annotations                                       |       |
| A refined decompiler to generate C code with high readability                     |       |
| Deeply Reifying Running Code for Constructing a Domain-Specific Language          |       |
| Multi-Target C Code Generation from MATLAB                                        |       |
| A Heterogeneous Parallel Framework for Domain-Specific Languages                  |       |
| When and how to develop domain-specific languages                                 |       |
| Morphing: Safely Shaping a Class in the Image of Others                           |       |
| Metaprogramming with Traits                                                       |       |
| JavaGI: Generalized Interfaces for Java                                           |       |
| Scala Macros: Let Our Powers Combine!                                             |       |
| Modular Domain Specific Languages and Tools                                       |       |
| MontiCore: a framework for compositional development of domain specific languages |       |
|-----------------------------------------------------------------------------------+-------|

```

# Shallow Embedding of DSLs via Online Partial Evaluation

* Shallow Embedding - Immediately translate to target language (Code string)
* Deep Embedding - Build data structure of decision tree (JSON string)
* Explot domain knowledge
* Utilize features in target hardware
* Staging

# Apache Flink: Stream and Batch Processing in a Single Engine

* DataSet and DataStream API
* FlinkML (ML), Gelly (Graph), Table (SQL)
* Client and Job Manager
* DataFlow graph (DAG)
  * Stateful operators (subtasks)
  * Datastreams (stream partitions)
* Optimizations
* Control events (injected by operators into data stream)
  * Checkpoint barriers
  * Watermarks
  * Iteration barriers
* Memory Management
  * Serialize instead of allocating on JVM heap
  * => Reduces garbage collection overhead

# The Rust Programming Language 2

* Can achieve one thing in many ways (not like C)
* Dynamic linking is possible
* Basic features => Deep Embedding (For type checking)
* Complex features => Shallow Embedding (Can't cover them all)
* Mutability, scope, lifetime, pointers, pattern matching

# General purpose languages should be metalanguages

* Separate general purpose aspects from problem specific aspects
* Metalanguages

# Scala-virtualized

* Minimal suite of extensions to Scala
* Blends shallow and deep embedding => Low overhead, but AST-like representation
* Infix methods: x.a() => infix_a(x)
* Control flow method calls: if(a) b else c => ifThenElse(a,b,c)
* Reify static source information: Store information about invocation site in IR
  * Generated automatically with `implicit ctx: SourceLocation` argument
  * `SourceContext` gives information about parent (StackTrace)
  * Line number, char offset, file name, method name
* Delite

# Ziria: A DSL for wireless systems programming

* Ziria - "Stand-alone" language
* Vectorization - Optimize for data widths of components in processing pipelines
  * Cardinality Analysis
  * Identify => Top-down, Configure => Bottom-up
* Stream transformers and Stream computers

# Polymorphic Embedding of DSLs

* Seems to interpret DSLs into the Scala language, which might not be wanted
* Polymorphic Embedding:
  * Language Interfaces, Virtual types
  * Semantics (Subclass of Language Interface), maps Virtual Type to Domain
* Code generators:
  * Create custom pre-processor which converts DSL into program fragments of
    the host language, e.g, library function calls
  * High cost, not worth it when implementing small DSLs
* Embedded Interpreter:
  * Reuse host language parser and compiler
  * Create AST of embedded program, process it with interpreter in host language
  * Does not work when mixing DSLs in host-language expressions
* Pure Embedding:
  * Implement domain types as host language types
  * Implement domain operations as host language functions
  * DSLs agree on a common set of types
    * Makes it possible to use multiple DSLs simultaneously
  * Multiple interpretations of a DSL
    * Interpret for optimizations
    * Interpret for correctness checks
* Desirable Properties
  * **Reuse of infrastructure**: DSL reuses the host language.
  * **Pluggable semantics**: DSL can be interpreted in multiple ways.
  * **Static safety**: Check DSL against syntactic and contextual restrictions.
  * **Compositionality**: Loose coupling between DSL expressions.
  * **Composability**: Different DSLs can be integrated.
  * **Modular semantics**: Can construct DSL semantics.
  * **Performance**: Performance should be reasonable in large-scale use.
* Not every DSL needs to be typed
* Hierarchical definition of DSLs
