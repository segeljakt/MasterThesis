# Rain https://github.com/substantic/rain

Rain is an open source distributed computational framework, with a core written in Rust and an API written in Python. Rain aims to lower the entry barrier to the field of distributed computing by being portable, scalable and easy to use.Computation is defined as a task-based pipeline through dataflow programming. Tasks are coordinated by a server, and executed by workers which communicate over direct connections. Workers may also spawn subworkers as local processes. Tasks are either BIFs or UDFs. UDFs can execute Python code, and can make calls to external applications. Support for running tasks as plain C code, without having to link against Rain, is on the roadmap.

# Apache Arrow https://arrow.apache.org

Systems and libraries like Spark, Cassandra, and Pandas have their own internal memory format. When transferring data from one system to another, about 70-80% of the time is wasted on serialization and deserialization. Apache Arrow eliminates this overhead through a common in-memory data layer. Data is stored in a columnar format for locality which maps well to SIMD operations. Arrow is available as a cross-language framework for Java, C, C++, Python, JavaScript, and Ruby. It is currently supported by 13 large open source projects, including Spark, Cassandra, Pandas, Hadoop, and Spark.

# DataFusion https://www.datafusion.rs

DataFusion is a distributed computational platform, serving as a proof-of-concept for what Spark could be if it were to be implemented in Rust. Spark's scalability and performance is challenged by the overhead of garbage collection and Java object serialization. While Tungsten addresses these issues by storing data off-heap, the issues could be avoided altogether by transitioning away from the JVM. DataFusion provides functionality which is similar to Spark's SQL's DataFrame API, and takes advantage of the Apache Arrow memory format. DataFusion outperforms Apache Spark for small datasets, and is still several times faster than Spark when computation gets I/O bound. In addition, DataFusion uses less memory, and does not suffer from unforeseen garbage collection pauses or OutOfMemory exceptions.
