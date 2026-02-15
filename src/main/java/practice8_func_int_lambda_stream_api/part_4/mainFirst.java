package practice8_func_int_lambda_stream_api.part_4;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class mainFirst {
    public static void main(String[] args) {
        List<String> stringList = Arrays.asList("", "Test", "Test", "Testik", "Mail", "NoBug", "Offer", "NoBugs");
        final Map<Character, List<String>> stringGroupedByFirstSymbol = stringList.stream()
                .filter(str -> !str.isEmpty())
                .collect(Collectors.groupingBy(str -> str.charAt(0)));
        System.out.println(stringGroupedByFirstSymbol);
    }
}
