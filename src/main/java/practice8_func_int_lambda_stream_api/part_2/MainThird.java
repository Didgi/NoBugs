package practice8_func_int_lambda_stream_api.part_2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainThird {
    public static void main(String[] args) {
        List<String> strExample = Arrays.asList("Testik", "Tests", "Test");
        List<Integer> countSymbolsInString = strExample.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println(countSymbolsInString);
    }
}
