package practice8_func_int_lambda_stream_api.part_2;

import java.util.List;
import java.util.stream.Collectors;

public class Duplicates {

    public static <T> List<T> removeDuplicates(List<T> list) {
        return list.stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
