#[no_mangle]
pub extern "C" fn func(a: i32) -> (i32,i32,i32) {
  (a+1,a+2,a+3)
}
