\clearpage

# Introduction

<!--What is CDA?--> Deep Analytics, or Big Data Analytics, is the application of data intensive processing techniques in the field of data mining [@DeepAnalytics]. Data can come from multiple sources in a structured, semi-structured or unstructured format. Continuous Deep Analytics (CDA) is a new breed of analytics where data is also massive, unbound, and live [@ContinuousDeepAnalytics].

<!--What is this thesis about?--> This thesis is part of a five year project in collaboration between KTH and RISE SICS to develop a system capable of CDA [@ContinuousDeepAnalytics]. The CDA system must be able to run for years without interruption. It also needs to be capable of processing incoming queries in short time windows to support real-time, mission-critical, decision making. CDA is aimed towards both the public sector and industry, much like today's modern general purpose distributed systems. It will enable new time-sensitive applications such as zero-time defense for cyber-attacks, fleet driving and intelligent assistants. These applications involve machine learning and graph analytics, both of which require large scale, data intensive, matrix and tensor computations for affine transformations and convolutional operations [@DeepLearningBook]. There are two sides to the problem of supporting these kinds of heavy computations: hardware acceleration and computation sharing.

## Hardware Acceleration

<!--What are the problems with Hardware Acceleration?--> Hardware acceleration implies that the system exploits the available hardware resources to speedup computation. This is often not an easy task, since developers must have expertise with multiple APIs and programming models which interface with the drivers, e.g., CUDA, OpenCL, OpenMP and MPI [@Virtualization]. When interfaces change, developers need to update their code. As a further matter, machines in distributed systems can have various hardware configurations. The trend of scaling out, and adding new machines with different hardware, does not make things easier. Hence, hardware acceleration in the presence of hardware heterogeneity becomes an issue of maintenance when code for one machine is neither compatible nor portable to others.

<!--How do modern systems solve these problems? (Hardware Virtualization)--> The solution to the problem of hardware heterogeneity is hardware virtualization, which abstracts the physical hardware details away from the user [@Virtualization]. Spark and Flink realize hardware virtualization through the Java Virtual Machine (JVM) [@Spark][@Flink]. The JVM is portable, but its support for accelerator architectures, e.g., GPUs, is limited [@HJOpenCL]. Hence, the JVM forfeits support for hardware acceleration in favor of support for hardware heterogeneity. Moreover, it also has a runtime overhead, in part owed to garbage collection. To give an example, evaluation by [@PagerankEvaluation] has revealed that a laptop running single threaded low level code can outperform a 128 core Spark cluster in PageRank. High end graph stream processing systems, GraphLab and GraphX, were outperformed as well. The evaluation measured 20 PageRank iterations for two medium sized graphs, with the largest being ~105M nodes and ~3.5B edges. An industry standard benchmark by [@Flare] as well identified that Spark SQL spends close to 80% of its execution decoding in-memory data representations. Even when removing this layer of indirection, performance remains 30% slower than hand written C code.

<!--How will CDA approach this problem?--> The CDA system will try to obtain both portability and performance simultaneously through code generation. Instead of writing different code for different hardware, the user will write code which is generated for different hardware. Hence, the issue of maintainability is pushed to the developers of CDA rather than the users. An early overview of the system can be viewed in {@fig:CDA}. At the front-end, the user describes the desired behavior of the data processing program in a high level domain-specific language (DSL). The front-end code is then transformed into an intermediate representation (IR) containing information about the execution plan and cluster setup. Then, the execution plan is optimized logically through dataflow analysis, and physically by mapping tasks to machines. Next, low level code is generated and compiled for each task, tailored to its machine's hardware. Finally, binaries are deployed in the cluster.

![An overview of CDA.](CDA.png){#fig:CDA width=60% height=60%}

<!--How will the code generator work?--> The code generator will also be written as a DSL. Thereby, there are two DSLs involved, the front-end, user-facing, DSL, and the back-end, developer-facing, DSL. This thesis concerns the latter, which will be written as a library in a Scala. The code generator will receive tasks as input and translate low-level source code through the library's interface. How the code is assembled depends on the hardware resources of the machines subject to executing the tasks.

## Computation Sharing

<!--Computation Sharing--> Hardware aware code generation is only half of the problem CDA will tackle. The other half is computation sharing. Queries to the CDA system may contain user defined functions (UDFs) which appear as black boxes to the pipeline. The aim is to turn the black boxes into white boxes to allow for more fine grained optimizations. The current idea is to establish common IR, similar to Weld [@Weld], across libraries and languages. This will allow cross library optimizations, and translation of UDFs written in foreign languages into Rust code.

## Background

TO BE WRITTEN

## Problem

<!--Problem definition.-->
<!--Problem statement.-->
<!--References.-->

<!--What are problems with today's code generators?--> C and C++ are a commonly used as the language for low-level systems programming [@RustBelt]. While both compile to fast machine code, neither provide strong safety guarantees. Segmentation faults and memory leaks are recurrent errors. The CDA code generator will therefore instead emit Rust code. Rust is a recent programming language which achieves both safety and performance through a special memory management policy. The problem thus to implement a DSL for Rust code generation in Scala. Rust's advanced type system makes this a difficult task.

The DSL is meant to glue pieces of code together from different libraries. Therefore, the majority of the implementation will be in pure Rust.

<!--How is the project split?--> The problem is divided into two parts code. First, the code generator must have an interface, i.e., DSL, for expressing the Rust language in Scala. Two approaches will be explored, a shallow and deep embedding. The former concatenates and statically checks snippets of Rust source code. The latter represents the program as an AST which is subsequently traversed for Rust code generation. The second part is to turn the code into an executable which can be run on multiple platforms. This thesis focuses on the former part, while the latter is covered in another thesis by Oscar Bjuhr [@Oscar].

<!--What are the design goals--> The following design goals are expected for the DSL.

* ***Coverage*** - The DSL should support as much of Rust's syntax and semantics as possible.

* ***Static checking*** - The DSLs should be able to catch errors in the user's code.

* ***Consistency*** - The behavior of the generated program should be consistent with what was specified by the user.

<!--* ***Runtime error reporting*** - It should be possible to back-track the cause of runtime errors which occur in the generated program.-->

* ***Ease-of-use*** - Writing code in the DSL should be easy, with minimum boilerplate. Developers should also feel some familiarity

* ***Extensibility*** - The DSL should be extensible to the front-end user's for adding new structs and functions, etc.

<!--* ***Performance*** - The DSL should not take long time to execute as the driver must translate incoming IR into executables on the fly.-->

<!--Problem Statement--> The problem statement can be defined as: "How do you implement a DSL for Rust code generation in Scala which satisfies the design goals?".

## Purpose

<!--Motivation behind writing this report.--> Modern general-purpose distributed systems such as Spark suffer from performance degradation due placing the workload on the JVM [@Flare]. The purpose of this thesis is to explore code generation as a solution to these problems. Developers of future distributed systems may benefit from the discoveries. Another purpose is to motivate developers to write Rust code generators, rather than C or C++ code generators. As a consequence, future distributed systems might become more secure.

## Goal

<!--Concrete deliverables.--> This thesis will explore DSLs, Rust and Scala to develop a prototype back-end code-generator for the CDA project. Hence, the following deliverables are expected: 

* A background study of programming languages, Rust, Scala, and the theory behind DSLs.
* Two Scala DSLs for Rust code generation, and a description of their design and implementation.
* An evaluation of the DSLs, taking the design goals into consideration.

## Benefits, Ethics and Sustainability

CDA will improve upon existing state-of-the-art systems like Spark and Flink. Flink is being used by large companies including Alibaba, Ericsson, Huawei, King, LINE, Netflix, Uber, and Zalando [@FlinkUsecases]. Since performance is a crucial metric, these companies may benefit from incorporating CDA into their business. As an example, Alibaba uses Flink for optimizing search rankings in real-time. CDA may allow for more complex and data intensive search rank optimizations. This should benefit the customer, whom will have an easier time finding the right product. CDA's power however comes with a responsibility, as it can be used to either help, or harm others. Although one of the use cases for CDA is cyber defence, there is nothing preventing it from being used in the opposite way. Another concern is how CDA's possible contribution to artificial intelligence might impact the lives of people. With more computation power comes more well-trained AI. This could lead to more positions of employment being succumbed by artificial intelligence, e.g., intelligent assistants and driverless vehicles.

In this thesis' perspective, it crucial that the DSL is statically safe and does not generate buggy code which could compromise security. The behavior of the generated code should be what was specified in the IR. For sustainability, low level code is able to utilize hardware with higher efficiency than high level code. Thus, better performance in this case might implicate less waste of resources. As a result the power usage goes down, which is healthy and sustainable for the environment.

## Related Work

The related work describes Spark, Flare, Weld, ScyllaDB, Apache Arrow, DataFusion <!--, SystemML-->, and Voodoo.

### Spark [@Spark]

<!--What is Spark?--> Spark is a modern general purpose distributed system for batch processing. It was designed to get around the limitations of MapReduce. While MapReduce is able to perform large-scale computations on commodity clusters, it has an acyclic dataflow model which limits its number of applications. Iterative applications such as most machine learning algorithms, and interactive analytics are not feasible on MapReduce. Spark is able to support these features, while retaining the scalability and reliability of MapReduce. The core abstraction of Spark is a Resilient Distributed Dataset (RDD). A RDD is a read-only collection of objects partitioned over a cluster. The RDD stores its lineage, i.e., the operations which were applied to it, which lets it re-build lost partitions.

<!--What is Spark's API?--> RDDs support two forms of operations: transformations and actions [@RDD]. Transformations, e.g., map, and foreach, transform an RDD into a new RDD. Actions, e.g., reduce, and collect, returns the RDD's data to the driver program. All transformations are lazily evaluated. With lazy evaluation, data in a computation is materialized only when necessary. This speeds up performance by reducing the data movement overhead [@Weld].

<!--What is Spark SQL?--> Spark SQL is an extension to Spark which brings support for relational queries [@SparkSQL]. It introduces a DataFrame abstraction. Whereas RDDs are a collection of objects, DataFrames are a collection of records. DataFrames can be manipulated both with Spark's standard procedural API and with a new relational API. The relational API supports SQL written queries.

### Flare [@Flare]

<!--What is the most relevant piece related work?--> CDA's approach to code generation draws inspiration from Flare which is a back-end to Spark. Flare bypasses Spark's inefficient abstraction layers by compiling queries to native code, replacing parts of the Spark runtime, and by extending the scope of optimizations and code generation to UDFs. Flare is built on top of Delite which is a compiler framework for high performance DSLs, and LMS, a generative programming technique. When applying Flare, Spark's query performance improves and becomes equivalent to HyPer, which is one of the fastest SQL engines. 

### Weld [@Weld]

<!--Problem with libraries--> Libraries are naturally modular: they take input from main memory, process it, and write it back. As a side effect, successive calls to functions of different libraries might require materialization of intermediate results, and hinder lazy evaluation.

<!--How Weld solves it--> Weld solves these problems by providing a common interface between libraries. Libraries submit their computations in IR code to a lazily-evaluated runtime API. The runtime dynamically compiles the IR code fragments and applies various optimizations such as loop tiling, loop fusion, vectorization and common sub-expression elimination. The IR is minimalistic with only two abstractions: builders and loops. Builders are able to construct and materialize data, without knowledge of the underlying hardware. Loops consume a set of builders, apply an operation, and produce a new set of builders. By optimizing the data movement, Weld is able to speedup programs using Spark SQL, NumPy, Pandas and Tensorflow by at least 2.5x.

### ScyllaDB [@ScyllaDB]

NoSQL is a new generation of high performance data management systems for Big Data applications. The consistency properties of relational SQL systems limit their scalability options. In contrast, NoSQL systems are more scalable since they store data in flexible and replicable formats such as key-value pairs. One of the leading NoSQL data stores is Cassandra, which was originally developed by Facebook. Cassandra is written in Java and provides a customizable and decentralized architecture. ScyllaDB is an open-source re-write of Cassandra into C++ code with focus on utilization of multi-core architectures, and removing the JVM overhead.

Most of Cassandra's logic stays the same in ScyllaDB. Although, one notable difference is their caching mechanisms. Caching reduces the disk seeks of read operations. This helps decrease the I/O load which can be a major bottleneck in distributed storage systems. Cassandra's cache is static while ScyllaDB's cache is dynamic. ScyllaDB will allocate all available memory to its cache and dynamically evict entries whenever other tasks require more memory. Cassandra does not have this control since memory is managed by the JVM garbage collector. In evaluation, ScyllaDB's caching strategy improved the reading performance by less cache misses, but also had a negative impact on write performance.

### Rain [@Rain]

Rain is an open source distributed computational framework, with a core written in Rust and an API written in Python. Rain aims to lower the entry barrier to the field of distributed computing by being portable, scalable and easy to use.Computation is defined as a task-based pipeline through dataflow programming. Tasks are coordinated by a server, and executed by workers which communicate over direct connections. Workers may also spawn subworkers as local processes. Tasks are either BIFs or UDFs. UDFs can execute Python code, and can make calls to external applications. Support for running tasks as plain C code, without having to link against Rain, is on the roadmap.

### Apache Arrow [@ApacheArrow]

Systems and libraries like Spark, Cassandra, and Pandas have their own internal memory format. When transferring data from one system to another, around 70-80% of the time is wasted on serialization and deserialization. Apache Arrow eliminates this overhead through a common in-memory data layer. Data is stored in a columnar format, for locality, which maps well to SIMD operations. Arrow is available as a cross-language framework for Java, C, C++, Python, JavaScript, and Ruby. It is currently supported by 13 large open source projects, including Spark, Cassandra, Pandas, Hadoop, and Spark.

### DataFusion [@DataFusion]

DataFusion is a distributed computational and acts as a proof-of-concept for what Spark could be if it were to be re-implemented in Rust. Spark's scalability and performance is challenged by the overhead of garbage collection and Java object serialization. While Tungsten addresses these issues by storing data off-heap, they could be avoided altogether by transitioning away from the JVM. DataFusion provides functionality which is similar to Spark's SQL's DataFrame API, and takes advantage of the Apache Arrow memory format. DataFusion outperforms Spark for small datasets, and is still several times faster than Spark when computation gets I/O bound. In addition, DataFusion uses less memory, and does not suffer from unforeseen garbage collection pauses or OutOfMemory exceptions.

<!--### SystemML [@SystemML]-->

<!--TO BE WRITTEN-->

### Voodoo [@Voodoo]

Voodoo is a code generation framework which serves as the backend for MonetDB [@Voodoo]. MonetDB is a high performance query processing engine. Voodoo provides a declarative intermediate algebra which abstracts away details of the underlying hardware. It is able to express advanced programming techniques such as cache conscious processing in few lines of code. The output is optimized OpenCL code.

Code generation is complex. Different hardware architectures have different ways of achieving performance. Moreover, the performance of a program depends on the input data, e.g., for making accurate branch predictions. As a result, code generators need to encode knowledge both about hardware and data to achieve good performance. In reality, most code generators are designed to generate code solely for a specific target hardware. Voodoo solves this through providing an IR which is portable to different hardware targets. It is expressive in that it can be tuned to capture hardware-specific optimizations of the target architecture, e.g., data structure layouts and parallelism strategies. Additional defining characteristics of the Voodoo language are that it is vector oriented, declarative, minimal, deterministic and explicit. Vector oriented implicates that data is stored in the form of vectors, which conform to common parallelism patterns. By being declarative, Voodoo describes the dataflow, rather than its complex underlying logic. It is minimal in that it consists of non-redundant stateless operators. It is deterministic, i.e., it has no control-flow statements, since this is expensive when running SIMD unit parallelism. By being explicit, the behavior of a Voodoo program for a given architecture becomes transparent to the front end developer.

Voodoo is able to obtain high parallel performance on multiple platforms through a concept named controlled folding. Controlled folding folds a sequence of values into a set of partitions using an operator. The mapping between value and partition is stored in a control vector. High performance is achieved by executing sequences of operators in parallel. Voodoo provides a rich set of operators for controlled folding which are implemented in OpenCL. Different implementations for the operators can be provided depending on the backend.

When generating code, Voodoo assigns an Extent and Intent value to each code fragment. Extent is the code's degree of parallelism while Intent is the number of sequential iterations per parallel work-unit. These factors are derived from the control vectors and optimize the performance of the generated program.

## Delimitations

<!--What was intentionally left out?--> Only Rust will be used as the target language for code generation. It would be interesting to embed other languages, e.g., C and C++, but this is out of scope for this thesis. In addition, the code generator will support many, but not all, of Rust's features. Instead, the focus is quality over quantity.

## Methodology

No, please write something about your own, concrete methodology which means: 

* how did you do it
* why did you do it the way you did it.

The first step of the thesis was to gain an understanding of the problem which needed to be solved.

The first step of the thesis was to do a literature study of work related to distributed systems, code generation, and DSLs. The purpose was to gain insight into the problem the thesis aims to solve and also explore existing solutions. Afterwards, the objective was to study Rust and Scala, through reading the documentations, practicing, and talking with the community. The intent was to gain a deep enough understanding to be able to design and implement a DSL in Scala for generating Rust code. With some prior experience in C and C++, the only difficult part of learning Rust was understanding how borrow checking works. Scala was harder to learn since it is very different from other programming languages. Following, the two DSLs were prototyped, and underwent multiple revisions before settling on the final design. Both designs were initially suggested by the supervisors. After implementation, the DSLs were evaluated. About 60% of the thesis was spent on reading. The remaining parts were A major portion of the thesis was reading. The remaining portion was divided roughly in half to implementing and writing the report.

## Outline

<!--What is covered in each section?.--> Section 2 gives a bird's eye view over the area of programming languages and domain specific languages. Embedding one language in another requires in-depth knowledge about how both languages operate. Section 3 therefore covers Rust's syntax and semantics, and aims to answer the question of what is needed to embed Rust in another language. Section 4 sheds light on Scala's features and modern approaches for embedding DSLs, to identify if it can meet Rust's demands. The design of the Rust DSLs is covered in section 5. Section 6 contains implementation details, and section 7 evaluates the implementations, in the form of a demonstration. Section 8 discusses the results with respect to the design goals. Finally, section 9 concludes with mentions about future work.

