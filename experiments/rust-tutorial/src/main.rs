/******************************************************************************
*     File Name           :     main.rs                                       *
*     Created By          :     Klas Segeljakt <klasseg@kth.se>               *
*     Creation Date       :     [2018-02-07 10:54]                            *
*     Last Modified       :     [2018-02-07 22:50]                            *
*     Description         :                                                   *
******************************************************************************/
struct X<'a> {
  a: &'a str,
  b: &'a str,
}

fn foo<'x>(a: &'x str, b: &'x str) -> &'x str {
  a
}

fn main() {
  //let x = vec![1,2,3,4];
  //let mut y = &x;
  //{
    //let z = vec![2,3,4,5];
    //y = &z;
  //}
  
  let s1 = "abc";
  let s2 = "def";
  let x = X {a: &s1, b: &s2};
  foo(&s1, &s2);

}
