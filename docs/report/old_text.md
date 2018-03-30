

# FRONT-END DSL

<!--Central addressed issue of this thesis--> The front end of systems such as Spark and Flink consists of a query interface. The interface provides a set of transformers and actions. Users can also define custom user defined functions (UDFs).

# OPTIMIZATIONS

For a given query, the code generator must both optimize each stage of the query and the query plan as a whole. Another issue is User Defined Functions (UDFs). UDFs are black boxes whose functionality might not be known at compile time. The task of optimizing these is a monumental challenge, and will be left out for future work. Another topic of interest is whether code also could be generated for the network layer. Most modern day switches are programmable, and could allow for further optimizations [@SOURCE].

<!--Challenges--> A possible approach to mitigate the performance loss, while still maintaining portability is to use a domain specific language (DSL) [@Delite]. DSLs are minimalistic languages, tailor-suited to a certain domain. They bring domain-specific optimizations which general-purpose languages such as Java are unable to provide. DSLs can optimize further by generating code in a low level language, and compiling it to binary code which naturally runs faster than byte code. C and C++ are commonly used as the target low level language. We regard Rust as a candidate as it provides safe and efficient memory management through ownership.

Previous work by [@SOURCE] has shown that Spark's performance can be improved to be much faster through the use of DSLs. There is no equivalent solution yet for Flink, and this thesis aims to address this. The problem can be summarized as the following problem statement: How can Apache Flink's performance be improved through a Rust DSL?

The program generator is written in a meta-language, and generates hardware-sensitive code for a target-language.

The code generator is written programmed with a DSL. DSLs are minimalistic languages, tailor-suited to a certain problem domain.

# SCALABILITY

There are two general approaches to scaling a distributed system: scaling up and scaling out [@Scale-up x Scale-out: A Case Study using Nutch/Lucene]. Scaling up involves modifying existing machines to make them more powerful. This can be expensive, but also effective as it is almost guaranteed to improve performance. Scaling out instead adds new machines to the network rather than modifying existing ones. It can be cheaper since the new machines do not have to be more powerful. Although, this approach leads to rise in data center bills and power consumption [@Flare], which can be expensive. Another downside of scaling up is the performance loss due to the increase in network communication and coordination for each machine added.

# VIRTUALIZATION

The CDA system, like other distributed systems, will distribute its workers over multiple machines. Multiple workers may execute in parallel on the same machine. Workers expect to have exclusive access to their machine's low level hardware resources. With virtualization, it becomes easier to distribute these resources among workers executing concurrently on the same machine. Virtualization software prevents resource conflicts between operative systems. 

<!--[@https://superuser.com/questions/333297/is-it-possible-to-dual-boot-two-oss-at-the-same-time]-->
https://cloud.google.com/tpu/
