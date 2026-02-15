package practice8_func_int_lambda_stream_api.part_3;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class MainMax {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 10, 1, 10);
        Integer maxNumber = numbers.stream()
                .max(Integer::compareTo)
                .orElseGet(() -> {
                    System.out.println("пустое значение");
                    return null;
                });
        System.out.println(maxNumber);
    }
}
