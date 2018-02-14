# RustBelt: Securing the Foundations of the Rust Programming Language

Rust is a new programming language designed to overcome the tradeoff between the safety of high-level languages and control of low-level languages. The core design behind Rust is ownership. Previous approaches to ownership were either too restrictive or too expressive. Rust's ownership discipline is that an object cannot be mutated while being aliased. This eliminates the risk for data races. In cases where this model is too restrictive, Rust provides unsafe operations for mutating aliased state. Safety of these operations cannot be ensured by the Rust compiler.

While Rust is safe without using unsafe operations, it is questionable how safe its libraries are. Many Rust libraries, including the standard library, makes use of unsafe operations. RustBelt (λ~Rust~) is an extension to Rust which verifies the soundness of code which uses unsafe operations. It builds a semantic model of the language which is then verified against typing rules. A well-typed program should not express any undefined behavior.

Keywords: RustBelt, unsafe, soundness, proof, nll
