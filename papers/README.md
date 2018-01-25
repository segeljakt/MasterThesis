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
| Polymorphic embedding of DSLs                                                     |       |
| Flare: Native Compilation for Heterogeneous Workloads in Apache Spark             |       |
| A Heterogeneous Parallel Framework for Domain-Specific Languages                  |       |
|-----------------------------------------------------------------------------------+-------|
| Language Virtualization for Heterogeneous Parallel Computing                      |       |
| Voodoo - A Vector Algebra for Portable Database Performance on Modern Hardware    |       |
| Weld: Rethinking the Interface Between Data-Intensive Libraries                   |       |
| Concealing the deep embedding of DSLs                                             |       |
| A Fast Abstract Syntax Tree Interpreter for R                                     |       |
| Code Generation with Templates                                                    |       |
| A Python Wrapper Code Generator for Dynamic Libraries                             |       |
| The Scala Programming Language                                                    |       |
| Data stream processing via code annotations                                       |       |
| A refined decompiler to generate C code with high readability                     |       |
| Deeply Reifying Running Code for Constructing a Domain-Specific Language          |       |
| Multi-Target C Code Generation from MATLAB                                        |       |
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
* Exploit domain knowledge
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

# Flare: Native Compilation for Heterogeneous Workloads in Apache Spark

* Flare - Bypass inefficient abstraction layers of Spark
  1. Compilation to native code
  2. Replacing parts of Spark runtime
  3. Extending the scope of optimization and code generation to UDFs
  * Small clusters, strong machines, faults are improbable
  * Could generate LLVM directly instead of C ~20-30% faster compilation
  * OpenMP ~= Pthreads in performance
  * Scaling up > Scaling out (easier to optimize)
* Heterogenous Workloads: Interleaving UDFs with DataFrame operations
* UDFs - Black boxes to the query optimizer
* Delite - Compiler framework for high performance DSLs, GPU
* TPC-H - Workload metric
* Scaling out = More inefficiently used machines
* Global warming
* Flare on 1 Thread >= Spark on 10 nodes and linear scaling
* Spark: RDD
  * Lazily evaluated, lineage
  * Limited visibility for optimizations, and Interpretive overhead
* Spark SQL: DataFrames
  * Query plan
  * Catalyst: Rule/cost based query optimizations
  * Tungsten: JVM allocation optimizations
  * Deferred API:
    1. Optimizations
    2. Front end API
    3. Host language integration
  * Multi-stage-programming
* Flare L1: Native compilation within Tungsten
  * Add rules to catalyst
  * Trigger Tungsten to invoke LMS to compile
  * JNI compiled code
* Flare L2: Compile whole queries instead of stages
  * Let users pick which DataFrames to optimize
  * Compiled CSV and Parquet readers, specialized to schema
  * OpenMP
  * NUMA, maximize local processing (avoid non-local memory)
* Flare L3: Delite, intermediate layer between query plan and generated code
  * Inject LMS into UDFs
    * Rep[T] (Override +, -, \*)
  * Delite is built on top of LMS, compiles mixes of DSLs
    * GPU
    * OptiQL API - Code generation
    * DMLL - Parallelization, heterogenous hardware optimizations
* Spark scales well because of internal overhead
* PageRank: Laptop > 128 core cluster
* Apache Parquet: Data representation
* Iterative Map-Reduce: *Twiter*, *Haloop*
* *Hive*, *Dremel*, *Impala*, *Shark*, *Spark SQL*
* *SnappyData*: *Spark* + Transactional Memory
* *Asterix*,*Stratosphere*/*Apache Flink*, *Tupleware*: Improves *Spark*
  * *Tupleware* integrates UDFs into the LLVM level
* Query compilation
  * Templates: *Daytona*, *HIQUE*
  * General purpose compilers: *HyPer*, *Hekaton*
  * DSL compiler frameworks: *Legobase*, *DryadLINQ*, *DBLAB*
* Embedded DSL frameworks and intermediate languages
  * *Voodoo*, *Delite*, *DMLL,* *Weld*, *Steno*
* Thesis: Generic high-performance DSL compiler framework for Flink
* Identify bottlenecks
  * Tight code
  * CPU (Not IO) is the source of bottlenecks
  * Single threaded programs outperform clusters
* "Modern data analytics need to combine multiple programming models and make e cient use of modern hardware with large memory, many cores, and accelerators such as GPUs"
* "We believe that multi-stage APIs, in the spirit of DataFrames, and compiler systems like Flare and Delite, will play an increasingly important role in the future to satisfy the increasing demand for  exible and uni ed analytics with high e ciency."

# A Heterogeneous Parallel Framework for Domain-Specific Languages

* Delite compiler framework and runtime
* Heterogenous parallel architecture programming difficulty =>
  * Productivity loss, Maintainability, Scalability, Portability
  * Need higher level abstractions => DSL
    * Higher level than general programming languages
    * Enable domain specific optimizations
    * External (Independent), Internal (Embedded into host language)
      * Internal are easier, but sacrifice optimizations
  * Layer 1: LMS - Embed DSL into host language
    - Overrides expressions
  * Layer 2&3: Delite
    * Add domain-specific optimizations
    * Generate IR, which is consumed by runtime
    * Extensible compilation framework - don't need to write custom compiler for DSL
      * Need to implement Delite data/control structures
      * Need to add domain specific optimization rules
    * Provides extensible parallel patterns
    * Generic IR - Symbols and definitions, "Sea of nodes", CSE, transformations
      * Better optimizations than compiler because of domain knowledge
      * Assumption: Operations are side-effect free and objects start out as immutable
    * Parallel IR - Delite *op*, track dependencies among nodes
      * (Sequential,Reduce,Map,Zip,MultiLoop)
    * Domain-specific IR - Extend Delite *op*
    * Heterogenous code generation - Extend *code generators*, can override for library
      * Execution graph of Delite ops with executable kernels
      * Compile code in multiple ways (GPU/CPU)
    * Runtime <- Execution graph, kernels, other code
      * Clustering algorithm: Schedule each op on the same resource as one of its inputs
      * CPU: Scala, GPU: Cuda, JNI as bridge
      * GPU memory management
* DSL compilers vs DSL libraries
  * DSL libraries: JNI native binary, hard to get execution graph
  * DSL compilers: Code generation, easy to get execution graph
* Metaprogramming:
  * Techniques: C++ templates, Template Haskell, Expression Templates, TaskGraph
  * Languages: Telescoping Languages, MetaML, MetaOCaml
  * Libraries: ATLAS, FFTW, SPIRAL
* Heterogenous programming: EXOCHI, OpenCL, Merge, Harmony
* Data-parallel programming:
  * Copperhead, FlumeJava, Intel's Array Building Blocks, Concurrent Collections (CNC)
  * DryadLINQ (LINQ = DSL, Dryad = Runtime)
* Parallel programming languages:
  * Chapel, Data-Parallel Haskell, Fortress, High Performance Fortran, NESL, X10

# CITATION NEEDED

* Wikipedia (https://en.wikipedia.org/wiki/Domain-specific_language)
  * domain-specific languages are less comprehensive.
  * domain-specific languages are much more expressive in their domain.
  * domain-specific languages should exhibit minimal redundancy.
* JVM does not work well with GPU
* Rust can be faster than C in some cases (due to ownership)
* Add info about why project is currently missing an IR
* 90% of the execution is for 10% of the code
