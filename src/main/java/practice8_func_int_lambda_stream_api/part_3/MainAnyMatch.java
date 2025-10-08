package practice8_func_int_lambda_stream_api.part_3;

import java.util.Arrays;
import java.util.List;

public class MainAnyMatch {

    public static boolean isContainEvenNumber(List<Integer> numbersList) {
        return numbersList.stream()
                .anyMatch(n -> n % 2 == 0);
    }

    public static void main(String[] args) {
        List<Integer> numbersWithEven = Arrays.asList(1, 2, 3, 4, 5, 6);
        List<Integer> numbersWithourEven = Arrays.asList(1, 3, 5);
        System.out.println(isContainEvenNumber(numbersWithEven));
        System.out.println(isContainEvenNumber(numbersWithourEven));
    }
}
