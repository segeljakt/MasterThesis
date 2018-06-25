\clearpage

# Conclusion

Code generation DSLs will be a crucial component in the next generation of distributed systems for exploiting the resources of heterogeneous hardware. Rust's safety guarantees, combined with its zero cost abstractions, makes it a promising target language. Meanwhile, Scala's rich type system and flexibility for embedding DSLs makes it a favorable host language. This thesis has explored and demonstrated two techniques for embedding Rust in Scala. The first is basic string interpolation. The second technique combines heterogeneous lists with fluent interfaces and algebraic data types to produce a DSL which is capable of declaring customized structs and functions.
