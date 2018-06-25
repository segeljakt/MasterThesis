\clearpage

# The Rust Programming Language

<!--What are the problems with C & Java?--> C and C++ have for decades been the preferred languages for low level systems programming [@SFI_IFC]. Both offer excellent performance, but are also unsafe. Mistakes in pointer aliasing, pointer arithmetic and type casting, leading to memory violations, can be hard to detect, even for advanced software verification tools. Recurrent errors are memory leaks, null pointer dereferences, segmentation faults, and data races. Although C++ facilitates countermeasures, e.g., smart pointers, RAII, and move semantics, its type system is too weak to statically enforce their usage [@RustBelt]. Meanwhile, safe high-level languages like Java solve the safety issues through managed runtime coupled with a garbage collector. This safety comes at a cost since garbage collection incurs a big overhead. Overcoming the tradeoff between safety and control has long been viewed as a holy grail in programming languages research.

<!--What is Rust, and what makes it special?--> Rust is a modern programming language conceived and sponsored by Mozilla [@RustBelt]. It overcomes the tradeoff between safety and control through a compile time memory management policy based on ownership, unique references, and lifetimes. Ownership prevents double free errors, unique references prevent data races, and lifetimes prevent dangling pointers. In addition, Rust offers zero-cost abstractions such as pattern matching, generics, traits, higher order functions, and type inference.

Packages, e.g., binaries and libraries, in Rust are referred to as crates [@RustReference, ch. 4]. Cargo is a crate manager for Rust which can download, build, and publish crates. A large collection of open-source crates can be browsed at [^1]. One of the largest crates to date is the Servo browser engine, developed by Mozilla. Servo's strict demands for security and memory safe concurrency have attributed to shaping Rust into what it is today [@RustOreilly, ch. 1].

Rust has a stable, beta, and nightly build [@Rust1, ch. 4]. The nightly build is updated on a daily-basis with new experimental features. Once every six weeks, the latest nightly build is promoted to beta. After six additional weeks of testing, beta becomes stable. Since Rust's original release, there have been multiple major revisions. Dropped features include a typestate system [@RustTypestate], and a runtime with green threaded-abstractions [@RustRuntime].

[^1]: `https://www.crates.io`.

## Basics

A Rust crate is a hierarchy of modules [@Rust1, ch. 3]. Modules contain structs, traits, methods, enums, etc., collectively referred to as items. Items support parametric polymorphism, i.e., generics. [@Lst:struct] defines a `Rectangle`{.Rust} struct and a `Triangle`{.Rust} tuple struct with fields of generic type `T`. Structs encase related values, and tuple structs are a variation of structs with unnamed fields.

```{#lst:struct .rust caption="Struct and tuple struct"}
struct Rectangle<T> {
  width:  T,
  height: T,
}
struct Triangle<T>(T, T, T);
```

Enums are tagged unions which can wrap values of different types. For example, `Shape`{.Rust} in [@lst:enum] wraps values of type `Rectangle`{.Rust} and `Circle`{.Rust}.

```{#lst:enum .rust caption="Enum."}
enum Shape<T> {
  Rectangle(Rectangle<T>),
  Triangle(Triangle<T>),
}
```

Traits define methods for an abstract type `Self`{.Rust}, and are implemented in ad-hoc fashion, comparable to type classes in other programming languages. In [@lst:trait], `Geometry`{.Rust} is a trait which defines a method for calculating the `perimeter`{.Rust}. `Rectangle`{.Rust}, `Triangle`{.Rust} and `Shape`{.Rust} implement the `Geometry`{.Rust} trait. Functions return the last expression in the function body, and as a result not require an explicit `return`{.Rust} statement.

```{#lst:trait .rust caption="Trait and implementations."}
trait Geometry<T> {
  fn perimeter(&self) -> T;
}

impl<T: Add<Output=T>+Copy> Geometry<T> for Rectangle<T> {
  fn perimeter(&self) -> T {
    self.width + self.width + self.height + self.height
  }
}

impl<T: Add<Output=T>+Copy> Geometry<T> for Triangle<T> {
  fn perimeter(&self) -> T {
    self.0 + self.1 + self.2
  }
}

impl<T: Add<Output=T>+Copy> Geometry<T> for Shape<T> {
  fn perimeter(&self) -> T {
    match self {
      &Shape::Rectangle(ref r) => r.perimeter(),
      &Shape::Triangle(ref t)  => t.perimeter(),
    }
  }
}
```

Note how the implementations require traits to be implemented for the generic types. `T: Add<Output=T>`{.Rust} requires a trait for addition to be implemented for `T`{.Rust}. `Output=T`{.Rust} implicates the result of the addition is of type `T`{.Rust} and `Copy`{.Rust} permits `T`{.Rust} to be copied. In the implementation for `Shape`{.Rust}, a `match`{.Rust} expression is used to unwrap the enum into references of its values.

[@Lst:main] defines the main function for testing the code. First, a closure, i.e., lambda function, `calc` is defined for calculating and printing the perimeter of a shape. It takes a `kind` argument, indicating whether the shape is a `Rectangle` or `Triangle`, and an array `v` storing the sides of the shape. The `!` in `println!` indicates `println` is a macro and not a method.

```{#lst:main .rust caption="Closures, struct and enum initialization, method and macro invocation, and pattern matching."}
fn main() {
  let calc = |kind: &str, v: &[i32]| {
    let shape = match kind {
      "Rectangle" => Shape::Rectangle(Rectangle{ width: v[0], height: v[1] }),
      "Triangle"  => Shape::Triangle(Triangle( v[0], v[1], v[2])),
      _           => std::process::exit(-1)
    };
    println!("Perimeter of {} is {}", kind, shape.perimeter());
  };
  calc("Rectangle", &[5,7]);  // Perimeter of Rectangle is 24
  calc("Triangle", &[3,3,3]); // Perimeter of Triangle is 9
}
```

## Syntax

Rust's syntax is mainly composed of *expressions*, and secondarily *statements* [@RustReference, ch. 6]. Expressions evaluate to a value, may contain operands, i.e., sub-expressions, and can either be mutable or immutable. Unlike C, Rust's control flow constructs are expressions, and can thereby be *side-effect* free. For instance, loops can return a value through the break statement. Expressions are either *place expressions* or *value expressions*, commonly referred to as *lvalues* and *rvalues* respectively. Place expressions represent a memory location, e.g., array an indexing, field access, or dereferencing operation, and can be assigned to if mutable. Value expressions represent pure values, e.g., literals, and can only be evaluated.

Statements are divided into *declaration statements* and *expression statements*. A declaration statement introduces a new name for a variable or item into a namespace. Variables are by default declared immutable, and are visible until end of scope. Items are components, e.g., enums, structs and functions, belonging to a crate. Expression statements are expressions which evaluate to the unit type by ignoring their operands' return results, and in consequence only produce side-effects. [@Lst:rust5] displays examples of various statements and expressions.

```{#lst:rust5 .rust caption="Rust's statements and expressions."}
struct Foo;              // Item declaration
let foo = Foo;           // Let declaration
loop { v.pop(); break; } // Expression statement
loop { break v.pop(); }  // Value expression
(1+2)                    // Value expression
*(&mut (1+2))            // Place expression
foo                      // Place expression
```

## Ownership

When a variable in Rust is bound to a resource, it takes *ownership* of that resource [@SFI_IFC]. The owner has exclusive access to the resource and is responsible for dropping, i.e., de-allocating, it. Ownership can be *moved* to a new variable, which in consequence breaks the original binding. Alternatively, the resource can be *copied* to a new variable, which results in a new ownership binding. Variables may also temporarily *borrow* a resource by taking a reference of it. The resource can either be mutably borrowed by at most one variable, or immutably borrowed by any number of variables. Thus, a resource cannot be both mutably and immutably borrowed simultaneously. The concept of ownership and move semantics relates to affine type systems wherein every variable can be used at most once [@AffineTypes].

Ownership prevents common errors found in other low level languages such as double-free errors, i.e., freeing the same memory twice. Moreover, the borrowing rules eliminate the risk of data-races. Although Rust is not the first language to adopt ownership, previous attempts were generally restrictive and demanded verbose annotations [@RustBelt]. Rust's ownership is able to solve complex security concerns such as Software Fault Isolation (SFI) and Static Information Control (IFC) [@SFI_IFC].

SFI enforces safe boundaries between software modules that may share the same memory space, without depending on hardware protection. If data is sent from a module, then only the receiver should be able to access it. This can get complicated when sending references rather than values in languages without restrictions to mutable aliasing. Rust's ownership policy ensures that the sent reference cannot be modified by the sender while it is borrowed by the receiver.

IFC imposes confidentiality by tracing information routes of confidential data. This becomes very complex in languages like C where aliasing can explode the number of information routes. IFC is easier in Rust because it is always clear which variables have read or write access to the data.

## Lifetimes

Every resource and reference has a lifetime which corresponds to the time when it can be used [@NLL][@Rust2]. The lifetime of a resource ends when its owner goes out of scope, and in consequence causes the resource to be dropped. Lifetimes for references can in contrast exceed their borrower's scope, but not their its referent's. A reference's lifetime can also be tied to others' [@AffineTypes]. For instance, a reference *A* to a reference *B* imposes the constraint that the lifetime of *A* must live for at least as long as the lifetime of *B*. Without this constraint, *A* might eventually become a dangling pointer, referencing freed memory.

Rust has a powerful type and lifetime inference which is local to function bodies. [@Lst:infertype] displays how Rust is able to infer the type of a variable based on information past its declaration site.

```{#lst:infertype .rust caption="Type inference example."}
fn foo() {
  let x = 3;
  let y: i32 = x + 5;
  let z: i64 = x + 5; // Mismatched types
}                     // Expected i64, found i32
```

Since the inference is not global, types and lifetimes must be annotated in item signatures as illustrated in [@lst:rust7][@Rust1, ch. 3]. Lifetimes in function signatures can however occasionally be concluded with a separate algorithm named *lifetime elision*. Lifetime elision adopts three rules. First, every elided lifetime gets a distinct lifetime. If a function has exactly one input lifetime, that lifetime gets assigned to all elided output lifetimes. If a function has a self-reference lifetime, that lifetime gets assigned to all elided output lifetimes. Cases when the function signature is ambiguous and the rules are insufficient to elide the lifetimes demand explicit lifetime annotations.

```{#lst:rust7 .rust caption="Type annotations, lifetime annotations, and lifetime elision."}
fn bar(x, y) -> _ { x }          // Does not compile
fn bar<T>(x: T, y: T) -> T { x } // Compiles

fn baz<T>(x: &T, y: &T) -> &T { x }             // Does not compile
fn baz<'a,T>(x: &'a T, y: &'a T) -> &'a T { x } // Compiles

fn qux<T>(x: &T) -> &T { x } // Compiles (Lifetime elision)
```

## Types

Rust has primitive, nominal, structural, pointer, function pointers, and closure types [@RustReference, ch. 7]. Primitive types include integers, floats, booleans, textual types, and the never type. Structs, unions and enums are nominal types. Nominal types can be recursive and generic. Arrays, tuples and slices are structural types, and cannot be recursive. Pointers are either shared references, mutable references, raw pointers or smart pointers. Function pointers identify a function by its input and output types. Closures have types as well, but hidden from the user.

There exists support for subtyping of lifetimes, but not structs [@RustSubtyping]. Naturally, it should be possible to use a subtype in place of its supertype. In the same sense, it should be possible to use a long lifetime in place of a shorter one. Hence, a lifetime is a subtype of another if the former lives for at least as long as the latter. Type theory formally denotes subtyping relationships by `<:`, e.g., `A <: B` indicates `A` is a subtype of `B`.

Rust's type system includes *type constructors* [@RustSubtyping]. A type constructor is a type which takes type parameters as input and returns a type as output, e.g., a generic nominal type `Option<T>`{.Rust} or pointer type `&'a mut T`{.Rust}. Types which take no type parameters are *proper types*. Type constructors can be covariant, contravariant, or invariant over their input. If `T <: U` implies `F<T> <: F<U>`, then `F` is covariant over its input. If `T <: U` implies `F<U> <: F<T>`, then `F` is contravariant over its input. `F` is invariant over its input if no subtype relation is implied. Immutable references are covariant over both lifetime and type, e.g., `&'a T`{.Rust} can be coerced into `&'b U`{.Rust} if `'a <: 'b` and `T <: U`. Contrarily, mutable references are variant over lifetime, but invariant over type. If type was covariant, then a mutable reference `&'a mut T`{.Rust} could be overwritten by another `&'b mut U`{.Rust}, where `'a <: 'b` and `T <: U`. In this case, `&'a`{.Rust} would eventually become a dangling pointer.

## Unsafe

Ownership and borrowing rules can in some cases be restrictive, specifically when trying to implement cyclic data structures [@LinkedList][@SFI_IFC]. For instance, implementing doubly-linked lists, where each node has a mutable alias of its successor and predecessor is difficult. There are in general two ways to achieve mutable aliasing. The first way is to use a reference counter (`Rc<T>`{.Rust}) together with interior mutability (`RefCell<T>`{.Rust}). The reference counter, i.e., smart pointer, allows a value to be immutably owned by multiple variables simultaneously. A value's reference counter is incremented whenever a new ownership binding is made, and decremented when one is released. If the counter reaches zero, the value is de-allocated. Interior mutability lets a value be mutated even when there exists immutable references to it. It works by wrapping a value inside a `RefCell`{.Rust}. Variables with a mutable or immutable reference to the `RefCell`{.Rust} can then mutably borrow the wrapped value. By combining reference counting with interior mutability, i.e., `Rc<RefCell<T>>`{.Rust}, multiple variables can own the `RefCell`{.Rust} immutably, and are able to mutably borrow the value inside.

The other way of achieving mutable aliasing is through unsafe blocks [@LinkedList][@SFI_IFC]. Unsafe blocks are blocks of code wherein raw pointers can be dereferenced. Raw pointers are equivalent to C-pointers, i.e., pointers without any safety guarantees. Multiple raw pointers can point to the same memory address. The compiler cannot verify the static safety of unsafe blocks. Therefore, code inside these blocks have the potential to cause segmentation faults or other undefined behavior, and should be written with caution. While Rust is safe without using unsafe operations, many Rust libraries including the standard library, use unsafe operations. Unsafe blocks are primarily used for making external calls to C. The support for calling C++ from Rust is limited however. RustBelt is an extension to Rust which verifies the soundness of unsafe blocks [@RustBelt]. It builds a semantic model of the language which is then verified against typing rules. A Rust program with well-typed unsafe blocks should not express any undefined behavior.

## Compiler overview

Rust's primary compiler is *rustc* [@rustc]. An overview of the pipeline for compiling source code into machine code is illustrated in [@fig:rustc].

![Overview of rustc.](rustc.png){#fig:rustc width=60% height=60%}

Lexing
: Rust's lexer distinguishes itself from other lexers in how its output stream of tokens is not flat, but nested [@rustc, ch. 10]. Separators, i.e., paired parentheses `()`, braces `{}`, and brackets `[]`, form token trees. Token trees are an essential part of the macro system. As a by-product, mismatched separators are among the first errors to be caught by the front-end. The lexer will also scan for raw string literals [@RawStringLiterals]. In normal string literals, special characters need to be escaped by a backslash, e.g., `" \" "`{.Rust}. Rust string literals can instead be annotated as raw, e.g., `r#" " "#`{.Rust}, which allows ommitting the backslash. For a string literal to be raw, it must be surrounded by more hashes than what it contains, e.g., `r#"##"#`{.Rust} would need to be rewritten to `r##"##"##`{.Rust}. The implication is that Rust's lexical grammar is neither regular nor context free as scanning raw string literals requires context about the number of hashes. For this reason, the lexer is hand written as opposed to generated.

Parsing
: Rust's parser is a recursive descent parser, handwritten for flexibility [@rustc, ch. 10]. A non-canonical grammar for Rust is available in the repository [@RustGrammar]. The lexer and parser can be generated with *flex* and *bison* respectively. While bison generates parsers for C, C++ and Java, flex only targets C and C++ [@Bison][@Flex]. JFlex is however a close alternative to Flex which targets Java [@JFlex]. The parser produces an AST as output that is subject to macro expansion, name resolution, and configuration. Rust's AST is atypical as it preserves information about the ordering and appearance of nodes. This sort of information is commonly stored in the parse tree and stripped when transforming into the AST.

Macro expansion
: Rust's macros are at a higher level of abstraction compared to standard C-style macros which operate on raw bytes in the source files [@rustc, ch. 11]. Macros in Rust may contain meta-variables. Whereas ordinary variables bind to values, meta-variables bind to token-trees. Macro expansion expands macro invocations into the AST, according to their definitions, and binds their meta-variables to token-trees. This task is commissioned to a separate regex-based macro parser. The AST parser delegates any macro definition and invocation it encounters to the macro parser. Conversely, the macro-parser will consult the AST parser when it needs to bind a meta-variable.

Configuration
: Item declarations can be prepended by an attribute which specifies how the item should be treated by the compiler. A category of attributes named *conditional compilation attributes* are resolved alongside macro expansion [@rustc, ch. 7][@Rust1, ch. 4]. Other are reserved for later stages of compilation. A conditional compilation attribute can for instance specify that a function should only be compiled if the target operating system is Linux. In consequence, the AST node for the function declaration will be stripped out when compiling to other operating systems. Compilation can also be configured by supplying compiler flags, or through special comment annotations at the top of the source file, known as *header commands* [@rustc, ch. 4].

Name resolution
: Macro expansion and configuration is followed by name resolution [@rustc, ch. 12]. The AST is traversed in top-down order and every name encountered is resolved, i.e., linked to where it was first introduced. Names can be part of three different namespaces: values, types, or macros. The product of name resolution is a name-lookup index containing information about the namespaces. This index can be queried at later stages of compilation. In addition to building an index, name resolution checks for name clashes, unused imports, typo suggestions, missing trait imports, and more. 

Transformation to HIR
: Upon finishing resolution and expansion, the AST is converted into a high-level IR (HIR) [@rustc, ch. 13]. The HIR is a desugared and more abstract version of the AST, which is more suitable for subsequent analyses such as type checking. For example, the AST may contain different kinds of loops, e.g., `loop`{.Rust}, `while`{.Rust} and `for`{.Rust}. The HIR instead represents all kinds of loops as the same `loop`{.Rust} node. In addition, the HIR also comes with a HIR Map which allows fast lookup of HIR nodes.

Type inference
: Rust's type inference algorithm is local to function bodies. It is based on the Hindley-Milner (HM) inference algorithm, with extensions for subtyping, region inference, and higher-ranked types [@rustc, ch. 15]. As input, the HM algorithm takes inference variables, also called existential variables, and unification constraints [@Chalk]. The constraints are represented as Herbrand term equalities. A Herbrand term is either a variable, constant or compound term. Compound terms contain subterms, and thus form a tree-like structure. Two terms are equated by binding variables to subterms such that their trees become syntactically equivalent. [@ConstraintPhD, @ConstraintLecture] Hence, the HM algorithm attempts to find a substitution for each inference variable to a type which satisfies the constraints. Type inference fails if no solution is found. Nominal types are equated by name and type parameters, and structural types by structure. Rust's inference variables are divided into two categories: type variables and region variables. Type variables can either be general and bind to any type, or restricted and bind to either integral or floating point types. Constraints for type variables are equality constraints and are unified progressively.

Region inference
: Region variables in contrast represent lifetimes for references [@rustc, ch. 15]. Constraints for region variables are subtype constraints, i.e., outlives relations. These are collected from lifetime annotations in item signatures and usage of references in the function body. Region inference is lazy, meaning all constraints for a function body need to be known before commencing the inference. A region variable is inferred as the lower-upper bound (LUB) of its constraints. The LUB corresponds to the smallest scope which still encompasses all uses of the reference. The idea is that a borrowed resource should be returned to its owner as soon as possible after its borrowers are finished using it. Lifetimes in Rust are currently lexical. Thereby, a lifetime, or region, is always bound to some lexical scope. This model will be changed in the near future to non-lexical lifetimes (NLL) which allow for more fine-grained control [@NLL]. NLL are resolved through liveness analysis. Thus, a NLL ends when its value or reference is no longer live, i.e., when it will no longer be used at a later time. While it is possible to determine a lexical lifetime through the HIR, NLLs are derived from the MIR.

Trait resolution
: During trait resolution, references to traits are paired with their implementation [@rustc, ch. 16]. Generic functions can require parameters to implement a certain trait. The compiler must verify that callers to the function pass parameters that fulfill the obligation of implementing the trait. Trait implementations may as well require other traits to be implemented. Trait resolution fails either if an implementation is missing or if there are multiple implementations with equal precedence causing ambiguity.

Method lookup
: Method lookup involves pairing a method invocation with a method implementation [@rustc, ch. 17]. Methods can either be inherent or extensions. The former are those implemented directly for nominal types while the latter are implemented through traits, e.g., `impl Bar`{.Rust} and `impl Foo for Bar`{.Rust} respectively. When finding a matching method, the receiver object might need to be adjusted, i.e., referenced or dereferenced, or coerced to conform to the expected self-parameter.

<!--Variance inference-->
<!--: TO BE WRITTEN-->

Transformation to MIR
: After type checking finishes, the HIR is transformed into a heavily desugared Mid-Level Intermediate Representation (MIR). MIR resembles a control flow graph of basic blocks. Thus, there is no more nested structure, and all types are known to the compiler.

<!--Borrow Checking-->
<!--: TO BE WRITTEN-->

<!--## Non-lexical Lifetimes-->


<!--Ownership is enforced by the Borrow Checker. The soundness of the Borrow Checker has formally been proven to be correct in a research project named Patina [@Patina].-->


