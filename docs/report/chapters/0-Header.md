---
title: A Scala DSL for Rust code generation
titleimg: kth.png
subtitle: Master Thesis
fontsize: 12pt
author:
  - Klas Segeljakt \<klasseg@kth.se\>
affil:
  - The Royal Institute of Technology (KTH)
geometry: margin=2.54cm
toc: yes
lot: yes
lof: yes
lol: yes
date: 'July 1, 2018'
multicols: yes
graphics: yes
link-citations: true
intitute:
  - The Royal Institute of Technology (KTH)
keywords: Continuous Deep Analytics, Domain Specific Langauges, Code Generation, Rust, Scala
header-includes:
  - \usepackage{listings}
  - \usepackage{color}
  - \newcommand{\hideFromPandoc}[1]{#1}
  - \hideFromPandoc{\let\Begin\begin \let\End\end}
abstract: "<!--Introduction, Problem--> Continuous Deep Analytics is a new form of analytics with performance requirements exceeding what the current generation of distributed systems can offer. This thesis is part of a five year project in collaboration between RISE SICS and KTH to develop a next generation distributed system capable of CDA. The two issues which the system aims to solve are *hardware acceleration* and *computation sharing*. The former relates to how the back-end of current generation general purpose data processing systems such as Spark and Flink runs on the Java Virtual Machine (JVM). As the JVM abstracts over the underlying hardware, its applications become portable but also forfeit the opportunity to fully exploit the available hardware resources. Computation sharing refers to how BigData and machine learning libraries such as TensorFlow, Pandas and Numpy must collaborate in the most efficient way possible. This thesis aims to explore the area of DSLs and code generation as a solution to hardware acceleration. The idea is to translate incoming queries to the system into low-level code, tailor suited to each worker machine's specific hardware. To this end, two Scala DSLs for generating Rust code have been developed for the translation step. Rust is a new, low-level programming language with a unique take on memory management which makes it as safe as Java and fast as C. The first DSL is implemented as a string interpolator. The interpolator splices strings of Rust code together, at compile time or runtime, and passes the result to an external process for static checking. The second DSL instead provides an API for constructing an abstract syntax tree (AST), which after construction can be traversed and printed into Rust source code. The API combines three concepts: heterogeneous lists, fluent interfaces, and algebraic data types. These allow the user to express advanced Rust syntax such as polymorphic structs, functions, and traits, without sacrificing type safety."
abstrakt: N/A  
---

\clearpage

\section*{\centering{Acronyms}}

| Acronym | Definition                     |
|---------+--------------------------------|
| CDA     | Continuous Deep Analytics      |
| AST     | Abstract Syntax Tree           |
| IR      | Intermediate Representation    |
| DSL     | Domain Specific Language       |
| GPL     | General Purpose Language       |
| JVM     | Java Virtual Machine           |
| UDF     | User Defined Function          |
| FOAS    | First Order Abstract Syntax    |
| HOAS    | Higher Order Abstract Syntax   |
| SFI     | Software Fault Isolation       |
| IFC     | Static Information Control     |
| HIR     | High-level IR                  |
| MIR     | Mid-level IR                   |
| ADT     | Abstract Data Type             |
| GADT    | Generalized Abstract Data Type |

\clearpage

\section*{\centering{Acknowledgements}}

I would first like to thank my examiner, Prof. Christian Schulte, and thesis supervisors, PhD. Lars Kroll, PhD. Paris Carbone and Prof. Seif Haridi, for giving me the opportunity to be part of the CDA project. Over the course of the thesis, their expert advice and guidance has been invaluable. I would also like to thank my colleagues Johan Mickos and Oscar Bjuhr for their insightful comments and inspiring dedication to their theses. Finally, I would like to thank the Rust and Scala community on Gitter for helping me reach a deeper understanding of both languages.

