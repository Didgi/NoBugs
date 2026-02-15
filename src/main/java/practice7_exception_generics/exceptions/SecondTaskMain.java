package practice7_exception_generics.exceptions;

public class SecondTaskMain {

    public static void division(int a, int b) {
        if (b == 0) {
            throw new RuntimeException("Деление на ноль запрещено");
        }
        System.out.println(a / b);
    }

    public static void main(String[] args) {
        division(5, 5);
        division(5, 0);

    }
}
