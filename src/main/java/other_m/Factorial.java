package other_m;

public class Factorial {
    public static void main(String[] args) {
        getFactorial(5);

    }

    public static void getFactorial(int num) {
        int result = 1;
//        if (num == 0 || num == 1) result = 1;
            for (int i = 2; i <= num; i++) {
                result *= i;
            }
        System.out.println(result);

    }
}
