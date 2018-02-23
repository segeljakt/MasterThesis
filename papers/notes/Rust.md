# System Programming in Rust: Beyond Safety

For many years, C has been used as the main-goto low-level system development language. While C is very optimized, it is also unsafe. Mistakes in pointer aliasing, pointer arithmetic and typecasting are hard to find, even for advanced software verification tools. Meanwhile, high level languages solve the safety issues through a runtime which employs garbage collection. This safety comes at a cost since garbage collection incurs a big overhead.

Rust is a relatively new low-level programming language. It achieves both performance and safety through an ownership memory management model. In addition, Rust provides many zero-cost abstractions, e.g., pattern matching, generics, traits, and type inference, which brings its utility closer to that of high level languages. Since its original release, Rust has seen multiple major revisions. Originally, it had a class system and a garbage collection memory model.

In Rust, when a variable is bound to an object, it takes ownership of that object. Ownership can be transferred to a new variable, which in consequence breaks the original binding. Naturally, an object cannot be aliased unless it has a reference counter. Objects are dropped, i.e., de-allocated, when their owner variable goes out of scope. A variable can temporarily borrow ownership of an object without breaking the binding.

Upfront, ownership might appear to be more of a restriction than a benefit compared to other memory models. Other languages might be better for prototyping. Albeit the restrictiveness, ownership can solve complex security concerns such as Software Fault Isolation (SFI) and Static Information Control (IFC).

Software fault isolation (SFI) enforces safe boundaries between software modules. A module should not be able to access the another's data without permission. As an example, C can violate SFI since a pointer in one module could potentially access another module's heap space. In contrast, Rust's ownership policy ensures that an object in memory is naturally accessible by only one pointer. Ownership, and information, can securely be transferred between modules without violating SFI.

Static information control (IFC) enforces confidentiality by tracing information routes of private data. This becomes very complex in languages such as C where aliasing can explode the number of information routes. Meanwhile, IFC is easier in Rust due to its aliasing restrictions.

Related work: Ownership is theft: experi- ences building an embedded OS in rust
              Redox - Your Next(Gen) OS
              NetBricks: Taking the V out of NFV
              Servo web browser engine
