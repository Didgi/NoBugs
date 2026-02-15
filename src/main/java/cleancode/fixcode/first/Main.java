package cleancode.fixcode.first;

public class Main {
    public static void main(String[] args) {
        MathOperations mathOperations = new MathOperations();
        System.out.println(mathOperations.add(1, 2, 3));
        System.out.println(mathOperations.add(1, 2, 3, 4, 5));
    }
}
