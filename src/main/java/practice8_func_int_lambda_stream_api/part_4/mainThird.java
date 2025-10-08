package practice8_func_int_lambda_stream_api.part_4;

import java.util.Arrays;
import java.util.List;

public class mainThird {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
        double averageNumber = numbers.stream()
                .reduce(0, Integer::sum) / (double) numbers.size();
        System.out.println(averageNumber);
    }
}
