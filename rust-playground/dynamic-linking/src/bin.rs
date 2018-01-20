extern crate dylib;

use std::mem::transmute;
use std::path::Path;

fn main() {

  // Link library
  let libpath = Path::new("../targets/libdynamic.dylib");
  let lib = match dylib::DynamicLibrary::open(Some(libpath)) {
    Ok(x) => x,
    Err(x) => panic!("{}", x),
  };

  // Get function
  let libfunc: extern "C" fn(i32) -> (i32,i32,i32) = unsafe {
    match lib.symbol::<u8>("func") {
      Ok(x) => transmute(x),
      Err(x) => panic!("{}", x),
    }
  };

  // Execute
  let input = 5;
  let (r1,r2,r3) = libfunc(input);

  println!("libfunc({}) => ({},{},{})", input, r1,r2,r3);
}
