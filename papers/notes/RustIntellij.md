# Language Injection

We use the Language Injection feature provided by IntelliJ for writing code inside string literals. Language Injection injects a programming language into a string literal, e.g., HTML, CSS, SQL, RegExp [@https://www.jetbrains.com/help/idea/using-language-injections.html]. IntelliJ will then provide syntax highlighting and linting inside the string literal for the injected language. Language Injection can be enabled manually through the IntelliJ interface, or by annotating the string literals. The `IntelliJ Rust` plugin can be installed to add support for Rust Language Injection, since native IntelliJ does not have it [@https://intellij-rust.github.io].

In Fig X, Rust Language Injection for a Scala string literal is enabled by adding a comment annotation on the line preceding the string literal.

```scala
def main(args: Array[String]): Unit = {
  // language=Rust
  """
  fn main() {
    println("Hello world");
  }
  """ 
}
```

