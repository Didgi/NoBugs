package practice8_func_int_lambda_stream_api.part_3;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MainMin {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 10, 1, 10);
        Integer minNumber = numbers.stream()
                .min(Comparator.naturalOrder())
                .get();
        System.out.println(minNumber);
    }
}
