/******************************************************************************
*     File Name           :     main.rs                                       *
*     Created By          :     Klas Segeljakt <klasseg@kth.se>               *
*     Creation Date       :     [2018-04-15 15:07]                            *
*     Last Modified       :     [2018-06-04 10:53]                            *
*     Description         :                                                   *
******************************************************************************/
extern crate syn;

use std::env;
use std::io::{self, Read};
use std::process;
use syn::*;

fn parse_node<T:synom::Synom>(buf: &str) {
  if let Err(e) = parse_str::<T>(&buf) {
    eprintln!("{}", e);
    process::exit(1);
  }
}

fn parse(node_type: String) -> io::Result<()> {
  let mut buf = String::new();
  io::stdin().read_to_string(&mut buf)?;

  match node_type.as_ref() {
    "file" => parse_node::<File>(&buf),
    "expr" => parse_node::<Expr>(&buf),
    "item" => parse_node::<Item>(&buf),
    "type" => parse_node::<Type>(&buf),
    _ => {
      eprintln!("ERROR: Unknown node type: {:?}", node_type);
      process::exit(1);
    },
  }
  Ok(())
}

fn main() {
  let mut args = env::args();
  let _ = args.next(); // Skip executable name

  let node_type = match args.next() {
    Some(node_type) => node_type,
    None => "file".to_owned(),
  };
  match parse(node_type) {
    Ok(()) => {},
    Err(_) => {
      eprintln!("ERROR: Parsing failed");
      process::exit(1);
    }
  }
}


//fn main() {
  //let x = 3;
  //let y: i32 = x + 5;
  //let z: i64 = x + 5; // Mismatched types
//}                     // Expected i64, found i32

// Does not compile
//fn foo(x, y) -> _ { x }
// Compiles
//fn foo<T>(x: T, y: T) -> T { x }

// Does not compile
//fn bar<T>(x: &i32, y: &i32) -> &i32 { x }
// Compiles
//fn bar<'a,T>(x: &'a T, y: &'a T) -> &'a T { x }

// Lifetime elision
//fn qux<T>(x: &T) -> &T { x }

//struct Foo;
//impl Foo {
  //fn x(&self) -> f32 { 3.3 }
//}
//trait Bar {
  //fn x(&self) -> i32 { 3 }
//}
//trait Baz {
  //fn x(&self) -> i32 { 3 }
//}
//impl Bar for Foo{}
//impl Baz for Foo{}

//fn main() {
  //let q = Foo;
  //let a = 3;
  //let w = a+q.x();
  //println!("{}", w);
//}
