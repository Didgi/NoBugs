package practice_1;

public class MathOperations {

    public static int add(int x, int y) {
        return x + y;
    }

    public static int subtract(int x, int y) {
        return x - y;
    }

    public static int multiply(int x, int y) {
        return x * y;
    }

    public static double divide(int x, int y) {
        return (double) x / y;
    }

    public static int findMax(int a, int b) {
        return a > b ? a : b;
    }

    public static int difference(int x, int y) {
        int diff1 = x - y;
        return diff1 >= 0 ? diff1 : diff1 * -1;
    }

    public static int squareArea(int side) {
        return side * side;
    }

    public static int squarePerimeter(int side) {
        return side * 4;
    }

    public static float convertSecondsToMinutes(int seconds) {
        return (float) seconds / 60;
    }

    public static double averageSpeed(double distance, double time) {
        return distance / time;
    }

    public static double findHypotenuse(double a, double b) {
        double sumSquares = Math.pow(a, 2) + Math.pow(b, 2);
        return Math.sqrt(sumSquares);
    }

    public static double circleCircumference(double radius) {
        double pi = Math.PI;
        return 2 * pi * radius;
    }
    public static double calculatePercentage(double total, double part) {
        return total / part * 100;
    }

    public static double celsiusToFahrenheit(double c) {
        return c * 9 / 5 + 32;
    }

    public static double fahrenheitToCelsius(double f) {
        return (f - 32) * 5 / 9;
    }


}
