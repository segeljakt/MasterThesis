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

* General introduction to the area.


<!--## Background-->

<!--* Brief background-->
<!--* References-->

## Problem

* Problem definition.
* Problem statement.
* References.

Current distributed systems are not able to support continuous deep analytics (CDA) at a large scale. In addition, distributed systems are becoming more heterogenous [@SOURCE]. This requires developers of CDA to have expertise with multiple APIs and programming models which interface with the drivers, e.g., CUDA, OpenCL, OpenMP, MPI. Because of this complexity, many general-purpose distributed systems, e.g., Spark and Flink, are written in high level languages such as Java and Scala. This in turn sacrifices performance for clarity, as running applications on the JVM incurs considerable overhead.

A possible approach to avoid performance loss is code generation. Code generation 

## Purpose

* Motivation behind writing this report.

The purpose of this thesis is to provide a useful resource for developers of distributed systems. It is specifically aimed at those who seek to boost the performance of distributed systems which are built on top of the JVM.

## Goal

* Concrete deliverables.

* A code generator for Rust written in Scala. The specifics are not yet decided as there are many different solutions to this problem.

## Benefits, Ethics and Sustainability

* References.

## Related Work

* What are the existing solutions?

## Delimitations

* What was intentionally left out?
* References.

## Contributions

* Concrete deliverables.

<!--## Methodology/Methods-->

<!--* Research methodology.-->

## Outline

* Description of report.

# Background

# Method

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



\End{multicols*}

