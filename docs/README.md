```
╭────────╮1
│ Client │
╰───┬────╯
    │
╭───┴────╮1
│ Driver │
╰───┬────╯
    │
╭───┴────╮*
│ Worker │
╰────────╯
```

# Client

* Scala
* Front end
* User code
* Generates IR
* IR must contain

# Driver

* Scala
* Scala-Kompics
* Interprets IR to Rust AST
* Generates Cargo project (Code + crates) from AST
* Builds executables for every worker (Hardware-dependent)

# Worker

* Rust
* Rust-Kompics
* Dynamically links
