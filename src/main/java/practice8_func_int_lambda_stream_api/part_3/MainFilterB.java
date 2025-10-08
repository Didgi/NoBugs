package practice8_func_int_lambda_stream_api.part_3;

import java.util.Arrays;
import java.util.List;

public class MainFilterB {
    public static void main(String[] args) {
        List<String> strExample = Arrays.asList("Арбуз", "Абрикос", "Банан", "Буженина");
        strExample.stream()
                .filter(n -> n.startsWith("Б"))
                .findFirst()
                .ifPresent(System.out::println);
    }
}
