package practice8_func_int_lambda_stream_api.part_2;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainFirst {
    public static void main(String[] args) {
        List<String> strExample = Arrays.asList("Testik", "Tests", "Test");
        List<String> filteredStr = strExample.stream()
                .filter(n -> n.length() > 5)
                .collect(Collectors.toList());
        System.out.println(filteredStr);
    }
}
