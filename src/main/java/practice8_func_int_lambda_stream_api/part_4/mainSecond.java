package practice8_func_int_lambda_stream_api.part_4;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class mainSecond {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        final Map<String, List<Integer>> numbersGroupedByEvenOdd = numbers.stream()
                .collect(Collectors.groupingBy(number -> number % 2 == 0 ? "Чётные" : "Не чётные"));
        System.out.println(numbersGroupedByEvenOdd);
    }
}
