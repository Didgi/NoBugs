package practice8_func_int_lambda_stream_api.part_2;

import java.util.Arrays;
import java.util.List;

public class MainFifth {
    public static void main(String[] args) {
        List<String> strExample = Arrays.asList("Test", "Test", "Testik", "Testik1", "Testik");
        List<Integer> intExample = Arrays.asList(1, 2, 3, 2, 3, 1, 5);
        System.out.println(Duplicates.removeDuplicates(strExample));
        System.out.println(Duplicates.removeDuplicates(intExample));
    }
}
