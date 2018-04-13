# Language Injection

Language Injection is a feature provided in the IntelliJ IDEA editor for metaprogramming. It enables injecting syntax highlighting and linting for a programming language into a string literal, e.g., HTML, CSS, SQL, RegExp [@https://www.jetbrains.com/help/idea/using-language-injections.html]. Language Injection can be enabled manually through the IntelliJ interface, or by annotating the string literals. The `IntelliJ Rust` plugin can be installed to add support for Rust Language Injection [@https://intellij-rust.github.io]. In Fig X, Rust Language Injection for a Scala string literal is enabled by adding a comment annotation on the line preceding the string literal.

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

