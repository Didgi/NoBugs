package practice8_func_int_lambda_stream_api.part_1;

public class MathOperationsMain {
    public static void main(String[] args) {
        MathOperation additional = Integer::sum;
        MathOperation subtraction = (a, b) -> a - b;
        MathOperation multiplication = (a, b) -> a * b;
        MathOperation division = (a, b) -> a / b;

        System.out.println("Сложение: " + additional.apply(5, 5));
        System.out.println("Вычитание: " + subtraction.apply(5, 5));
        System.out.println("Умножение: " + multiplication.apply(5, 5));
        try {
            System.out.println("Деление: " + division.apply(5, 5));
        } catch (RuntimeException e) {
            System.out.println("Деление на 0 запрещено: " + e.getMessage());
        }
    }
}
