\clearpage

# Evaluation

The evaluation is in the form of a demonstration, testing the features and static checking capabilities of the two DSLs.

## Shallow Embedded DSL - Validation testing

The runtime and compile time interpolators are evaluated with validation testing. Code for the tests is displayed in listing [@lst:tests]. Each test applies an interpolator `X` to a string, which splices it with arguments and then checks it with option `Y`. `X` and `Y` are the control variables. `X` is substituted with either `rrust` or `crust`, and `Y` is replaced by either `"file"` or `"item"`. Hence, each test is run with four variations. The tests can be described as:

* **Test 1**: Interpolate a string by splicing it with a string literal, into a statically correct string, and then check it. The result is asserted.
* **Test 2**: Interpolate a string by splicing it with a string literal, into a syntactically incorrect string, and then check it.
* **Test 3**: Interpolate a string by splicing it with a string literal, into an statically incorrect, but syntactically correct string, and then check it.
* **Test 4**: Interpolate a string by splicing it with the result of a compile time interpolator, into a statically correct string, and then check it.

```{#lst:tests .Scala caption="Shallow DSL validation tests"}
// Test 1
val a = "i32"
assert(X"fn main() { let x: $a = 3; }"(Y) == "fn main() { let x: i32 = 3; }")

// Test 2
val b = ";"
X"fn main() { let x: $b = 3; }"(Y)

// Test 3
val c = "f32"
X"fn main() { let x: $c = 3; }"(Y)

// Test 4
val d = crust"(1+2)"("expr")
X"fn main() { let x = $d; }"(Y)
```

The results of the tests are listed in [@tbl:results]. True positive (`TP`) means the interpolator reported an error when it should have. False positive (`FP`) means the interpolator reported an error when it should not have. True negative (`TN`) means the interpolator did not report an error when it should not have. False negative (`FN`) means the interpolator did not report an error when it should have. In short, `TP` and `TN` denotes expected behavior, and **`FN`** and **`FP`** denotes unexpected behavior. The error messages for some of the tests are included in appendix A.

| Test | rrust+rustc | rrust+syn | crust+rustc | rrust+syn |
|------+-------------+-----------+-------------+-----------|
| 1    | TN          | TN        | TN          | TN        |
| 2    | TP          | TP        | TP          | TP        |
| 3    | TP          | **FN**    | TP          | **FN**    |
| 4    | TN          | TN        | **FP**      | **FP**    |

: Results from the interpolator test. {#tbl:results}

## Deeply Embedded DSL - Demo

The deeply embedded DSL is evaluated by writing a program which combines the features described in the implementation. First, types and literals are declared in listing [@lst:demo1]

```{#lst:demo1 .scala caption="Types and literals."}
trait A
trait Rectangle[T] extends Polymorphic { type G = T::HNil }
val f = Lit[i32]("7")
val i = Lit[i32]("5")
```

Then, [@lst:demo2] declares a struct `Rectangle` with a generic width and height along with an implicit lookup function. The attributes and bounds are specified as strings.

```{#lst:demo2 .scala caption="Rectangle"}
def Rectangle[T:Show](x: Exp[T], y: Exp[T]) =
  Struct()
    .name[Rectangle[T]]
    .attrs("#[derive(Copy)]")
    .bounds("<A>")
    .field("x" ->> x)
    .field("y" ->> y)
implicit def RectangleDefinition[T:Show] = Rectangle[T](PH, PH)
```

[@Lst:demo3] defines a `perimeter` function for calculating the perimeter of a `Rectangle`.

```{#lst:demo3 .scala caption="perimeter"}
def perimeter[T:Show](r: Exp[Rectangle[T]]) =
  Fn()
    .name("perimeter")
    .bounds("<A:Add<Output=A>>")
    .arg("r" ->> r)
    .returns[T]
    .body { args =>
      args("r").f("w") + args("r").f("w") +
      args("r").f("h") + args("r").f("h")
    }
```

[@Lst:demo4] implements the `main` function which creates a `Rectangle` and calculates its `perimeter`.

```{#lst:demo4 .scala caption="main"}
def main =
  Fn()
    .name("main")
    .body { args =>
      Let(Rectangle(i, i)) { r =>
        perimeter(r) :#
      }
    }
```

Finally, [@lst:demo5] inserts the items into a `File` and generates code to a file `demo.rs`.

```{#lst:demo5 .scala caption="driver"}
def driver() = {
  val pw = new PrintWriter(new java.io.File("demo.rs"))

  File(
    Use("std::ops::Add;")
    Rectangle[A](PH, PH).decl,
    perimeter[A](PH, PH).decl,
    main.decl
  ).show(pw)

  pw.close()
}
```

Generated code [^2]:

[^2]: Appendix B includes the generated code after formatting it with `rustfmt`.

```
$ cat demo.rs
use std::ops::Add;
fn perimeter<A:Add<Output=A>>(x: Rectangle<A>, y: A) -> A {(x.a + y)}
struct Qux<A> {a: A, b: i32}
fn main() -> () {{let a = Qux { a: 1,b: 2 }; {let b = 5; foo(a,b); ()}}}
```

Compiling with `rustc`:

```
$ rustc output.rs
warning: field is never used: `b`
 --> output.rs:7:5
  |
7 |     b: i32,
  |     ^^^^^^
  |
  = note: #[warn(dead_code)] on by default
```


