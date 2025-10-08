package practice8_func_int_lambda_stream_api.part_1;

import java.util.function.Predicate;

public class MainPredicate {
    public static void main(String[] args) {
        Predicate<Integer> isNumberEven = (n) -> n % 2 == 0;
        System.out.println(isNumberEven.test(5));
        System.out.println(isNumberEven.test(4));
        System.out.println(isNumberEven.test(0));
    }
}
