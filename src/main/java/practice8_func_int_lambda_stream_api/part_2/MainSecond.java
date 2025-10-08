package practice8_func_int_lambda_stream_api.part_2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainSecond {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 19, 20);
        List<Integer> filteredNumbers = numbers.stream()
                .filter(n -> n % 5 == 0)
                .collect(Collectors.toList());
        System.out.println(filteredNumbers);
    }
}
