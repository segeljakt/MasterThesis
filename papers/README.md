```

| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ FINISHED ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ |
| Shallow Embedding of DSLs via Online Partial Evaluation                              |
| Apache Flink: Stream and Batch Processing in a Single Engine                         |
| The Rust Programming Language 2                                                      |
| General purpose languages should be metalanguages                                    |
| Scala-virtualized                                                                    |
| Ziria: A DSL for wireless systems programming                                        |
| Polymorphic embedding of DSLs                                                        |
| Flare: Native Compilation for Heterogeneous Workloads in Apache Spark                |
| A Heterogeneous Parallel Framework for Domain-Specific Languages                     |
| LMS: A Pragmatic Approach to Runtime Code Generation and Compiled DSLs               |
| Language Virtualization for Heterogeneous Parallel Computing                         |
| Paxos Made Switch-y                                                                  |
| Voodoo - A Vector Algebra for Portable Database Performance on Modern Hardware       |
| Suitability of NoSQL systems—Cassandra and ScyllaDB—for IoT workloads                |
| The Scala Programming Language                                                       |
| Weld: Rethinking the Interface Between Data-Intensive Libraries                      |
| Scala Macros: Let Our Powers Combine!                                                |
| System Programming in Rust: Beyond Safety                                            |
| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ PENDING ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ |
| SugarJ: library-based syntactic language extensibility                               |
| Transparent deep embedding of DSLs                                                   |
| Compiling Collection-oriented Languages onto Massively Parallel Computers            |
| Combining Deep and Shallow Embedding for EDSL                                        |
| Active Libraries and Universal Languages                                             |
| Programming Protocol-Independent Packet Processors                                   |
| Concealing the deep embedding of DSLs                                                |
| A Fast Abstract Syntax Tree Interpreter for R                                        |
| Code Generation with Templates                                                       |
| A Python Wrapper Code Generator for Dynamic Libraries                                |
| Data stream processing via code annotations                                          |
| A refined decompiler to generate C code with high readability                        |
| Deeply Reifying Running Code for Constructing a Domain-Specific Language             |
| Multi-Target C Code Generation from MATLAB                                           |
| When and how to develop domain-specific languages                                    |
| Morphing: Safely Shaping a Class in the Image of Others                              |
| Metaprogramming with Traits                                                          |
| JavaGI: Generalized Interfaces for Java                                              |
| Modular Domain Specific Languages and Tools                                          |
| MontiCore: a framework for compositional development of domain specific languages    |

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
* Heterogeneous parallel architecture programming difficulty =>
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

# LMS: A Pragmatic Approach to Runtime Code Generation and Compiled DSLs

* Requires Scala Virtualized !!
  * => https://pdfs.semanticscholar.org/e95b/9e1233b9090aaf319d5f4f78756ec7b9b7c5.pdf
  * http://web.stanford.edu/class/cs442/lectures_unrestricted/cs442-lms.pdf
* Generative programming - Program generator
  * Static - Compile time code generation
  * Dynamic - Runtime code generation
  * Multistage programming (MSP, staging) - Delay evaluation, Unpredictable
  * Quasi-quotation: Unevaluated code
* LMS
  1. Double => Rep[Double], Rep == Represents
  2. Wrap as trait (Expression, Definition, Block)
  * Embedding
    * *Tagless* (Compile time method resolution)
    * *Polymorphic* (Not implementation specific)
    * Safety - Representation inaccessible to generator
  * Override expressions, generic and specific optimizations
  * Separate type checking/compilation, ensure nonambiguity and exhaustiveness
  * Compile trait, traverse nested blocks and emit definition nodes
    * Eagerly expand function definitions
    * => Problem with recursion
    * Compare by serializing functions, (controlled unfolding), don't re-expand functions
  * "one cannot achieve clarity, safety, and efficiency at the same time"
  * Safety => Domain specific optimizations must happen before MSP code gen primitives
    * Keep internal code structure hidden from client generator code
* Higher Order AST (HOAS)

# Language Virtualization for Heterogeneous Parallel Computing

* Heterogenous parallelism => OpenMP, MPI, Cuda, OpenCL
* => Difficult, requires expertise, is thus sometimes avoided entirely by devs
* => Language Virtualization
* Limitations of General Purpose Languages:
  1. Must produce correct code for all applications => No aggressive optimizations
  2. Cannot infer about data structures (financial, gaming, image processing)
* DSLs: TeX/LaTeX, Matlab, SQL, OpenGL, Unix Shell
  * Can use aggressive optimizations
  * External: Implemented from scratch with custom compiler
    1. (-) Enormous effort
    2. (-) Cannot combine with other DSLs
  * Embedded: Lives inside host language
    1. (+) Easier for programmer (Does not need to learn new syntax)
    2. (+) Can combine with other DSLs
    3. (-) Cannot exploit domain knowledge => Solve with *language virtualization*
* Language is *virtualizable* if: e.g., Lisp
  * Expressiveness: Express DSL in a natural way to domain specialists
  * Performance: Should use domain knowledge to produce optimal code
  * Safety: Strict guarantees about generated program's behavior
  * Effort: Required effort should be close to that of embedded DSL
* Language virtualization
  * Similar to hardware virtualization
  * Consists of: 
    1. Application of DSLs
    2. DSLs embedded in Scala with language virtualization
    3. Scala-based compiler infrastructure, able to perform domain-specific optimizations
    4. Framework and runtime for DSL parallelization and mapping to heterogenous architectures
  * Expressiveness => Overload all constructs, e.g., ifThenElse()
  * Performance => Need AST, staged meta-programming, embedded object is analyzed by meta program
                   Selectively make constructs 'liftable'
                => Expression trees
  * Effort => Lightweight modular staging
  * Safety => Polymorphic embedding, tagless
  * Topology must remain constant for the program duration
  * Liszt: meshes and fields
    * Automatically perform domain decomposition (Partitioning, decompose mesh into optimal domains)
    * Automatic determination and maintenance of ghost cells (Minimize communication & sync.)
    * Selection of mesh representation (Choose optimal mesh data structure)
    * Optimize layout of field variables (Choose optimal field data structure)
  * OptiML: Devs can focus on accuracy and correctness, runtime manages performance and scaling
    * Stage-time optimizations
      * Transparent compression (Compress OptiML data structures before transfer)
      * Device locality (Use stage-time information to determine target device for kernel)
      * Choice of data representation (Sparse vs dense matrix)
    * Run-time optimizations
      * Best-effort computing (OptiML programmer tells runtime to trade accuracy for performance)
      * Relaxed dependencies (Trade accuracy for parallelism)
  * Delite - Simplify DSL parallelization
    * Provides set of predefined AST nodes for common parts of DSLs
    * Can add domain-specific nodes and semantics to the DSL
    * Provides case classes for parallel execution patterns (Map, Reduce, ZipWith, Scan)
    * Multiple stages of compilation,
    * Generates AST with virtualization and performs domain specific optimizations
    * Expands nodes to handle delite-specific implementation details and generic optimizations
    * Maps each domain-specific operation to hardware target => Optimized execution graph
  * Stage-time (Static), Run-time (Dynamic)
* Program generation
  * Given the desired parameters as input, outputs the corresponding specialized program
    * High performance libraries are built this way
  * Universal languages
  * MetaOCaml => Delineate staged expressions in a DSL (Separate host from generated program)
  * Running staged expressions => Assemble them as program source code
  * C++ Templates => Expansion at compile time
* Pure Embedding:
  * (+) Expressiveness, Safety, Effort
  * (-) Performance (Only partial evaluation)

```scala
trait TestMatrix { this: MatrixArith =>
  def example(a: Matrix, b: Matrix,
              c: Matrix, d: Matrix) = {
  val x = a*b + a*c
  val y = a*c + a*d
  println(x+y)
}

trait MatrixArith {
  type Rep[T]
  type InternalMatrix
  type Matrix = Rep[InternalMatrix]
  // allows infix(+,*) notation for Matrix
  implicit def matArith(x: Matrix) = new {
    def +(y: Matrix) = add(x,y)
    def *(y: Matrix) = mul(x,y)
  }
  def add(x: Matrix, y: Matrix): Matrix
  def mul(x: Matrix, y: Matrix): Matrix
}

trait Expressions {
  // constants/symbols (atomic)
  abstract class Exp[T]
  case class Const[T](x: T) extends Exp[T]
  case class Sym[T](n: Int) extends Exp[T]
  // operations (composite, defined in subtraits)
  abstract class Op[T]
  // additional members for managing encountered definitions
  def findOrCreateDefinition[T](op: Op[T]): Sym[T]
  implicit def toExp[T](d: Op[T]): Exp[T] = findOrCreateDefinition(d)
  // pattern-match on definitions
  object Def {
    def unapply[T](e: Exp[T]): Option[Op[T]] = ...
  }
}

trait MatrixArithRepExp extends MatrixArith with Expressions {
  //selecting expression tree nodes representation
  type Rep[T] = Exp[T]
  trait InternalMatrix
  //tree node classes
  case class Add(x: Matrix, y: Matrix) extends Op[InternalMatrix]
  case class Mul(x: Matrix, y: Matrix) extends Op[InternalMatrix]
  def add(x: Matrix, y: Matrix) = Add(x, y)
  def mul(x: Matrix, y: Matrix) = Mul(x, y)
}

trait MatrixArithRepExpOpt extends MatrixArithRepExp {
  override def add(x: Matrix, y: Matrix) =
  (x, y) match {
    // (AB + AD) == A * (B + D)
    case (Def(Add(a, b)), Def(Mul(c, d))) if (a == c) => Mul(a, Add(b,d))
    // calls default add() if no match
    case _ => super.add(x, y)
  }
}

object MyMatrixApp extends TestMatrix with MatrixArithRepExpOpt
```


# https://www.youtube.com/watch?v=16A1yemmx-w
* JVM => 10x - 1000x slowdown
* 100x faster if:
  * Don't ever use for loop
  * Don't ever use scala.collection.mutable
  * Don't ever use scala.collection.immutable
  * Always use Private[This]
  * Avoid closures
* 0 until x => allocates range (method call)
* Trend: We are moving away from the hardware
* GPU, Vector units, optimizing is huge effort
* DSL, different level of abstraction levels, different optimizations for each level
  * Front end: Matrix, Graph
  * Mid end: Array, Struct, Loop
  * Back end: SIMD, GPU, cluster
* Need extensibility and generic optimizations, (CSE, DCE)
* => LMS
  * Staging = Multi-Stage Programming, separate computations into stages
    1. "delay" expressions to a generated stage (do not run right now)
    2. "run" delayed expressions
    3. Staged program fragments as first class values
    * Think of it as generative metaprogramming
    * Program generically, run specialized
  * Core abstractions:
    * Int, String, T => Execute now!
    * Rep[Int], Rep[String], Rep[T] => Execute later
    * if (c) a else b -> ifThenElse(c,a,b) => Language virtualization
    * Extensible IR, transformers, loop fusion
    * reflect => Turn def into rep, Pure/Effect
* LMS IS A ***RUNTIME*** CODE GENERATION APPROACH

Lightweight Modular Staging (LMS) is a runtime code generation approach in Scala rooted in the idea of multistage programming. It uses domain knowledge to optimize the generated code. It is able to specialize the program after its input. The motivation behind LMS is that the JVM's bytecode runs 10 to 1000 times slower than low-level optimized code. Bytecode can be made to run 100 times faster by adopting the following rules: 1. Never use for-loops, 2. Never use `scala.collection.mutable` nor `scala.collection.immutable`, 3. Always use Private[This], and 4. Avoid closures, as they have the overhead of a method call.

LMS provides one core abstraction `Rep`, which can be used as `Rep[T]` to delay computation of an expression `T`. This causes it to not get evaluated at staging time. Staging time is the time before code generation. In consequence, code will be generated for `T`.

Meanwhile, a bare `T` indicates that `T` should not be delayed. By evaluating it at staging time, it becomes constant in the generated code and can be optimized away.

By dividing the execution into two stages, the abstractions of the first stage are abolished in the second stage. This will boost the performance of the application.

A usage example of LMS is for loop unrolling, see [@LISTING].

For example, `Rep[Range]` over a for-loop indicates that the loop should be unrolled. Compilers already provide this optimization at compile-time. The difference is that `Rep[Range]` unrolls at runtime, thus it can depend on dynamic input. The following code unrolls a loop if `n` is less than a `threshold`.

```.scala
if (n < threshold) {
  for (j <- (0 until n): Range) { // Unroll
    // Code
  }
} else {
  for (j <- (0 until n): Rep[Range]) { // Do not unroll
    // Code
  }
}
```

The reason for having a threshold is to limit the amount of code that can be generated. Generating too much code can speed down execution. Moreover, only when `n` is small does the overhead of allocating `Range` have an impact on the execution performance.



If the input is small, it is good to unroll the loop Normally, without the `Rep` abstraction, `T` would be executed as-is.

![@Yammer Moving from Scala to Java]

The 









# DSLs
* External => Custom compiler
* Internal (Embedded) => Piggyback on language
  * Shallow embedding => Code
  * Deep embedding => AST
  * Pure embedding => Library
  * Polymorphic embedding => Not library
* Any problem in computer science can be solved by another level of indirection
* Except problems caused by too many levels of indirection (Performance)

# CITATION NEEDED

* Wikipedia (https://en.wikipedia.org/wiki/Domain-specific_language)
  * domain-specific languages are less comprehensive.
  * domain-specific languages are much more expressive in their domain.
  * domain-specific languages should exhibit minimal redundancy.
* JVM does not work well with GPU
* Rust can be faster than C in some cases (due to ownership)
* 90% of the execution is for 10% of the code
* OpenCL = Embedded DSL, Cuda ~= External DSL

# Random Ideas

* Compiler generates optimal hardware/architecture for generated program
* Work stealing
* Implicit classes/def
* Conditionally generate fields (Do not generate struct fields if they are not used)
* C ABI
* def printAll(i: ReturnExp, strings: Exp*)

# Useful links

* Flink uses: https://cwiki.apache.org/confluence/display/FLINK/Powered+by+Flink


first-class citizen

Rust
* (+) Fast, ecosystem
* (-) Boilerplate, compile time

More approaches: external interpreter, pure/shallow/polymorphic/tagless embedding, dynamic/static generator, lightweight modular staging, Scala virtualized, ….
