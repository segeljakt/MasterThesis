\clearpage

# Appendix A - Error messages for Shallow DSL Validation Tests

This section lists the error messages for some tests to show what information they provide. The stack-trace is not shown for brevity.

## **Error message for Test 2 (rrust+rustc)**

```
Rust$RustException: error: expected type, found `;`
 --> tmp.rs:1:20
  |
1 | fn main() { let x: ; = 3; }
  |                 -  ^
  |                 |
  |                 while parsing the type for `x`

error: aborting due to previous error

>> fn main() { let x: ; = 3; } <<
```

## **Error message for Test 2 (rrust+syn)**

```
Rust$RustException: failed to parse item: failed to parse

>> fn main() { let x: ; = 3; } <<
```

## **Error message for Test 2 (crust+rustc)**

```
/Users/Klas/Thesis/experiments/interpolate/Test.scala:18: error: error: expected type, found `;`
 --> tmp.rs:1:20
  |
1 | fn main() { let x: ; = 3; }
  |                 -  ^
  |                 |
  |                 while parsing the type for `x`

error: aborting due to previous error


>> fn main() { let x: ; = 3; } <<

  crust"fn main() { let x: $y = 3; }"("file")
  ^
```

## **Error message for Test 4 (crust+rustc)**

```
/Users/Klas/Thesis/experiments/interpolate/Test.scala:28: error: Expected literal, found StringContext("(1+2)").cruste().
  crust"fn main() { let x = $c; }"
  ^
```
