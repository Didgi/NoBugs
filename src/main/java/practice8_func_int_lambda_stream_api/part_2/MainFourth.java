package practice8_func_int_lambda_stream_api.part_2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainFourth {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        List<Integer> squareNumbers = numbers.stream()
                .map(num -> num * num)
                .collect(Collectors.toList());
        System.out.println(squareNumbers);
    }
}
