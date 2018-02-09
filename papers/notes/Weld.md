# Weld: Rethinking the Interface Between Data-Intensive Libraries

Weld is an interface between different data-intensive libraries. Re-using code is a central part of software development. The field of distributed systems has a large eco-system of open-source software. As an example, libraries such as NumPy and Tensorflow are being used together in data intensive analytic applications. One of the most common optimizations in distributed processing is lazy evaluation. Through lazy evaluation, data of the distributed computation is materialized only when the computation is finished. This speeds up performance by reducing the data movement overhead.

Libraries are naturally modular: they take input from main memory, process it, and write it back. As a side effect, successive calls to functions of different libraries might require materialization of intermediate results, and hinder lazy evaluation.

Weld aims to solve this problem by bridging the gap between libraries through a common IR. The libraries submit their computations in IR code to a lazily-evaluated runtime API. The runtime dynamically compiles IR code fragments and applies optimizations such as loop tiling, loop fusion, vectorization and common subexpression elimination. The IR is minimalistic with only two abstractions: builders and loops. Builders are able to construct and materialize data, without knowledge of the underlying hardware. Loops consume a set of builders, apply an operation, and produce a new set of builders. By optimizing data movement, Weld is able to speedup programs using Spark SQL, NumPy, Pandas and Tensorflow by at least 2.5x.

* Keywords: Data movement optimizations, Loop tiling, IR, Marshalling
            Size analysis, loop fusion, vectorization & predication,
            common subexpression elimination, dynamic compilation

* Related: Steno: Automatic Optimization of Declarative Queries
           An introduction to SPIR-V
           Pydron: Semi-Automatic Parallelization for Multi-Core and the Cloud
           Spark SQL: Relational Data Processing in Spark
           Efficiently Compiling Efficient Query Plans for Modern Hardware
           An Architecture for Compiling UDF-centric Workflows

"Loop fusion: Fuse adjacent loops to avoid materializing intermediate results when the output of one loops is used as the input of another. Also fuses multiple passes over the same vector."

"Size analysis: Infers the size of output vectors statically."

"Loop tiling: Breaks nested loops into blocks to exploit caches by reusing values faster."

"Vectorization & predication: Transforms loops with simple inner bodies to use vector instructions. Branches inside the loop body are transformed into unconditional select instructions (predication)."

"Common subexpression elimination: Transforms the program to not run the same computation multiple times."
