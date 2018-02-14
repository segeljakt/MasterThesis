# Rust

# Variables

```
let x = 3;
let x: i32 = 3;
let mut x = 5;
```

# Tuples

```
let (x, y) = (1, 2);
let z = (3, 4);
let w = z.0;
```

# Functions

```
fn foo(bar: i32) -> i32 {
  bar*2
}
```

# Scope

```
{
  {

  }
}
```

# Primitives

```
bool, char, str,
i8, i16, i32, i64, i128
u8, u16, u32, u64
isize, usize
f32, f64
```

# Typecasting

```
let x: char = 3;
let y = x as i32;
```

# Comments

```
// Comment
/* Comment */
/// Documentation
//! Documentation
```

# Control flow

```
if x == 3 {
  // ...
} else if {
  // ...
} else {
  // ...
}
```

```
if let y = x == 3 { /* Code */ } else { /* Code */ };

loop {
  // ...
  break;
}

while true {
  // ...
}

for i in 0..5 {
  // ...
}

'outer': for i in 0..5 {
  'inner': for j in  0..10 {
    if ... {
      continue 'outer';
    }
  }
}
```

# Vectors

```
let v = vec![1,2,3];
let x = v[0];
```

# Ownership

```
fn foo(i: i32) {
  println("I copied {}", i);
}

fn bar(v: Vec<i32>) {
  println!("I own {}", v);
}

fn main() {
  let i = i32;
  let v = vec![1,2,3];
  foo(i); // Ok
  foo(i); // Ok (i32 has Copy trait)
  bar(v); // Ok
  bar(v); // Error (Do not have ownership of v)
}


-------------------------------------------------

let mut x = vec![1,2,3];
let y = &mut x; // Borrow x
y[0] = 3;

let z = &mut x; // Error (Can only borrow once)
let z = &x;     // Error (Can only borrow once)

-------------------------------------------------

let x = vec![1,2,3];
let y = &x;   // Borrow x
let z = &x;
let w = y[0];
y[0] = 3;     // Error (Immutable borrow)
```

# Lifetimes

```
// Contract
fn foo<'a, 'b>(s1: &'a str, s2: &'b str) -> &'a str {
  // ...
}

fn main() {
  let apple = "apple"       // ------------+ Allocate
  let x;                    //             |
  {                         //             |
    let banana = "banana";  // -+ Allocate |
    x = foo(apple, banana); //  |          |
  }                         // -+ Free     |
  println!("{}", x);        //             |
}                           // ------------+ Free
```

# Structs

```
pub struct Foo<'a> {
  x: i32,
  y: f64,
  z: &'a str, // struct cannot outlive 'a
}

fn main() {
  let q = "abc";
  let f = Foo {
    x: 3,
    y: 4.0,
    z: q,
  };
}
```

# Enums

```
enum Foo {
  Unknown,
  RGB(i32, i32, i32),
  Message(s: String),
}

fn main() {
  let q: Foo = Foo::RGB(100,200,30);
}
```

# Match

```
let x = 2;

let match x {
    1 => println!("one"),
    2 => println!("two"),
    _ => println!("something else"),
}
```

# Traits

```
trait Foo {
  fn foo(&self) -> bool;
}

trait FooBar : Foo {
  fn foobar(&self);
}

struct Baz {
  // ...
}

impl FooBar for Baz {
  fn foo(&self) {
    // ...
  }
  fn foobar(&self) {
    // ...
  }
}

fn main() {
  let x = Baz { ... };
  x.bar();
}
```

# Generics

```
struct Foo<T: Copy> {
  x: T,
  y: T,
}

impl<T> Foo<T> {
  ...
}

fn bar<U>(x: U) -> U {
  ...
}

#[derive(Debug)]
struct X {
  ...
}
```

# Trait Objects

```
pub struct TraitObject {
  pub data: *mut (),
  pub methods: *mut (),
}
```

# Closures

```
let a = 3;
let b = |x| x + 1;
let c = b(a);
```

# Modules

```
pub mod x {
  pub mod y {
    pub fn z() {
      ...
    }
  }
}

use x::y::z;

fn main() {
  x::y::z();
  z();
}
```

# Const and Static

```
const N: i32 = 5;  // Inlined
static M: i32 = 5; // Global

unsafe {
  M += 1;
}
```

# Attributes

```
#[inline(always)]
fn foo() {
  ...
}

mod bar {
  #![inline(always)]
}
```

# Type Aliasing

```
type X = i32;
let x: X = 99;
```

# Associated types

```
trait X {
  type A;

  fn foo(&self, &Self::A);
}

struct Y {
  ...
}

impl X for Y {
  type A = i32;

  fn foo(&self, &Self::A) {
    ...
  }
}
```

# Dynamic sized types

```
struct Foo<T: ?Sized> {
  x: T, // Dynamically sized
}
```

# Operator overloading

```
use::ops::Add;

struct Foo {
  x: i32,
  y: i32,
}

impl Add for Foo {
  type Output = Self;

  fn add(self, other: Foo) -> Self {
    Self { x: self.x+other.x, y: self.y+other.y }
  }
}

fn main() {
  let a = Foo { x: 2, y: 9 };
  let b = Foo { x: 3, y: 6 };
  let c = a+b;
}
```

# Dereferencing

# Macros

# Raw pointers

```
let x = 5;
let raw_const = &x as *const i32;
let raw_mut = &x as *mut i32;
unsafe {
  *raw_mut = 3;
  println!("{}", *raw_const);
}
```
