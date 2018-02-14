# Folding Domain-Specific Languages: Deep and Shallow Embeddings

Domain Specific Languages (DSLs) are small languages suited to interfacing with a specific domain. Domain contexts can be implemented directly in general purpose languages (GPLs) without the use of DSLs, but this would require many more lines of code.

DSLs can either be external or embedded. External DSLs exist in their own ecosystem, with their own compiler, debugger, and editor. Building and maintaining these tools require copious amounts of work. In contrast, embedded DSLs are are embedded within a host GPL. Embedded DSL's take less time to develop, but are also restricted in their expressiveness by the host GPL's syntax.

Embedded DSLs can either have a shallow or deep embedding. Shallow embedding implicates that the DSL is executed eagerly without constructing any form of intermediate representation. Deep embedding means that the DSL creates an intermediate abstract syntax tree (AST) which can be optimized before being evaluated.



DSLs: VHDL, SQL, PostScript
Standalone DSL = External DSL, requires separate ecosystem (Compiler,Editor,Debugger) (Favored by object oriented programmers)
Embed = Embed in GPL (Favored by functional programmers), deep and shallow embedding
