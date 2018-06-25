use std::ops::Add;
fn foo<A: Add<Output = A>>(x: Qux<A>, y: A) -> A {
    (x.a + y)
}
struct Qux<A> {
    a: A,
    b: i32,
}
fn main() -> () {
    {
        let a = Qux { a: 1, b: 2 };
        {
            let b = 5;
            foo(a, b);
            ()
        }
    }
}
