# Info

Adopted from

https://stackoverflow.com/questions/27358938/how-do-you-use-dynamiclibrary-with-a-dll-on-windows-in-rust

Try it out with `cargo run`.

`src/lib.rs` is compiled to a dynamic library: `targets/libdynamic.dylib`.
`src/bin.rs` dynamically links the dylib and executes its function.

