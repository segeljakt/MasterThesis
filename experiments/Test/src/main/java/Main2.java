import org.intellij.lang.annotations.Language;

public class Main2 {
    public static void main(String[] args) {

        @Language("Rust")
        String x =  "fn main() { x(3) }"
                  + "fn x() {  }"; // ERROR
        String z = x; // OK
        System.out.println("Hello world");
    }
}