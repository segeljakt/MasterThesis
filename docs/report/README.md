---
title: Hardware aware code generation for Rust
titleimg: kth.png
subtitle: Master Thesis
fontsize: 12pt
geometry: margin=2cm
author:
  - Klas Segeljakt \<klasseg@kth.se\>
toc: yes
lot: yes
lof: yes
affiliates:
  - Christian Schulte, Examinator
  - Paris Carbone, Supervisor
  - Lars Kroll, Supervisor
  - Johan Mickos, Student
  - Oscar Bjuhr, Student
multicols: yes
keywords: 
  - Hardware aware code generation
  - Abstract syntax trees
  - Rust
  - Apache Flink
header-includes:
  - \fancyfoot[C]{\thepage\ of \pageref{LastPage}}
  - \newcommand{\hideFromPandoc}[1]{#1}
  - \hideFromPandoc{
      \let\Begin\begin
      \let\End\end
    }
---

\newpage

\Begin{multicols*}{2}

# Abstract

# Introduction

* ***General introduction to the area.***

* => What is CDA?

Deep Analytics in the field of data mining is the application of data intensive processing techniques [@http://searchbusinessanalytics.techtarget.com/definition/deep-analytics]. Data can come from multiple sources in a structured, semi-structured or unstructured format. Continuous Deep Analytics (CDA) is a new breed of Deep Analytics where data is both massive, unbound, and live. Data needs to be processed in tight time windows to support real-time decision making. It will enable new time-sensitive applications such as zero-time defense for cyber-attacks and high-precision automated driving.

The CDA project is a five-year project by RISE SICS and KTH. 

<!--## Background-->

<!--* Brief background-->
<!--* References-->

## Problem

* ***Problem definition.***
* ***Problem statement.***
* ***References.***

* What is needed

CDA will need to run large scale matrix and tensor computations which are prevalent in machine learning and graph analytics. For this to work, the system needs be able to exploit the available hardware resources.

* Problem

Hardware acceleration is not easy. Developers need to have expertise with multiple APIs and programming models which interface with the drivers, e.g., CUDA, OpenCL, OpenMP and MPI [@Virtualization]. Moreover, machines in distributed systems can have various configurations. For example, when scaling out, one may choose to add new machines with better hardware. This becomes an issue as code which is optimized for on one architecture might not be portable to other architectures.

* Current solutions, and issues

In consequence, distributed systems make use hardware virtualization to abstract the physical hardware details from the user [@Virtualization]. Spark and Flink employ hardware virtualization through the Java Virtual Machine (JVM) [@Spark @Flink]. While the JVM is highly optimized, it is not well suited for interacting with the GPU [@SOURCE]. It also has a big runtime overhead, partially from garbage collection. Evaluation by [@Flare] has shown that a laptop running handwritten C code can outperform a 128 core native Spark cluster in PageRank. The evaluation measured 20 PageRank iterations for medium sized graphs of ~105M nodes and ~3.5B edges [http://law.di.unimi.it/webdata/uk-2007-05/].

* Possible solution

An approach to obtaining portability and performance at the same time is code generation. Instead of needing to write different code for different architectures, one can write code which generates code for different architectures. At the front-end, the user would use a high-level declarative language to describe the desired behavior of the program. Then, the code generator would generate an equivalent program in a low-level language tailor suited to a certain hardware configuration.

* Challenges

A possible approach to mitigate the performance loss, while still maintaining portability is to use a domain specific language Domain Specific Language (DSL) [@Delite]. DSLs are minimalistic languages, tailor-suited to a certain domain. They bring domain-specific optimizations which general-purpose languages such as Java are unable to provide. DSLs can optimize further by generating code in a low level language, and compiling it to binary code which naturally runs faster than byte code. C and C++ are commonly used as the target low-level language. We regard Rust as a candidate as it provides safe and efficient memory management through ownership.

For a given query, the DSL must both optimize each stage of the query and the query plan as a whole. Another issue is User Defined Functions (UDFs). UDFs are essentially black boxes whose functionality might not be known at compile time. The task of optimizing these is a monumental challenge, and will be left out for future work. Another topic of interest is whether code also could be generated for the network layer. Most modern day switches are programmable, and could allow for further optimizations [@SOURCE].

Previous work by [@SOURCE] has shown that Spark's performance can be improved to be much faster through the use of DSLs. There is no equivalent solution yet for Flink, and this thesis aims to address this. The problem can be summarized as the following problem statement: How can Apache Flink's performance be improved through a Rust DSL?

The program generator is written in a meta-language, and generates hardware-sensitive code for a target-language.

The code generator is written programmed with a DSL. DSLs are minimalistic languages, tailor-suited to a certain problem domain.

## Purpose

* ***Motivation behind writing this report.***

The purpose of this thesis is to provide a useful resource for developers of distributed systems. It is specifically aimed at those who seek to boost the performance of distributed systems which are built on top of the JVM.

## Goal/Contributions

* ***Concrete deliverables.***

The goal of this thesis is to explore the area of DSLs with respect to Rust and Flink. The following deliverables are expected:

* A Rust DSL written in Scala.
* An evaluation of Apache Flink's performance before and after integrating the Rust DSL.

## Benefits, Ethics and Sustainability

* ***References.***

Flink is being used by large companies such as Alibaba, Ericsson, Huawei, King, LINE, Netflix, Uber, and Zalando. As performance is often a crucial metric, these companies may see benefits in their business from the outcome of this thesis. As an example, Alibaba uses Flink for optimizing search rankings in realtime. By improving Flink's performance, it may allow for more complex and data intensive search rank optimizations. This will in consequence be beneficial for the customer whom will have an easier time finding their product.

Low level code is able to utilize hardware more efficiently than high-level code. Thus, better performance in this case also implicates less waste of resources. As a result the power usage goes down, which is healthy and sustainable for the environment.

In terms of ethics, it is crucial that the code generator does not generate buggy code which could compromise security. Keeping the code generation logic decoupled from the client code is also important. Without this consideration, an attacker would be able to generate malicious code to stage an attack with ease.

## Related Work

* ***What are the existing solutions?***

Our approach to optimizing Flink draws inspiration from Flare which is a back-end to Spark [@Flare]. Flare bypasses Spark's inefficient abstraction layers by 1. Compiling queries to native code, 2. Replacing parts of the Spark runtime, and by 3. Extending the scope of optimizations and code generation to UDFs. Flare is built on top of Delite, a compiler framework for high performance DSLs, and LMS, a generative programming technique. When applying Flare, Spark's query performance improves significantly and becomes equivalent to HyPer, which is one of the fastest SQL engines. 

Weld is a framework


## Delimitations

* ***What was intentionally left out?***
* ***References.***

Only Rust will be used as the target language for code generation. It would be interesting to compare its performance to C and C++, but this is out of scope for this thesis. Another delimitation is regarding the problem of optimizing UDFs. Existing solutions to this problem will be described in the background. The task of inventing a new solution, specialized for Flink, will be left for future studies.

<!--## Methodology/Methods-->

<!--* ***Research methodology.***-->

<!--## Outline-->

<!--* ***Description of report.***-->

# Background

Apache Flink is a streaming engine

## Domain Specific Languages (DSL)

## Code generation

* Generate binary directly, or generate Rust and compile?

### Lightweight Modular Staging (LMS)

### Delite

## Apache Flink

## Bottlenecks

## Hardware acceleration

# Design

# Implementation

# Evaluation

# Discussion

# Conclusions

```{.c caption="Hello"}
int main(int argc, char **argv) {
  return 0;
}
```


### H3 - b
Blah blah blah...

### H3 - c
Blah blah blah...




<!--[@Shallow @Flink @Rust @Scala-virtualized @Ziria @Polymorphic @Flare @Delite @LMS @Virtualization]-->
<!--@Folding-->
<!--@EDSL-->
<!--@ExpressionProblem-->
<!--@Weld-->
<!--@Scala-->
<!--@Voodoo-->
@ScyllaDB

\End{multicols*}

