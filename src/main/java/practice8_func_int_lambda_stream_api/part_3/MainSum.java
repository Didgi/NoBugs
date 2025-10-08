package practice8_func_int_lambda_stream_api.part_3;

import java.util.Arrays;
import java.util.List;

public class MainSum {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        int sumNumbers = numbers.stream()
                .mapToInt(n -> n)
                .reduce(0, Integer::sum);
        System.out.println(sumNumbers);
    }
}
