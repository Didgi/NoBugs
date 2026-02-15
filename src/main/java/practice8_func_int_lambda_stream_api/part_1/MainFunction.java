package practice8_func_int_lambda_stream_api.part_1;

import java.util.function.Function;

public class MainFunction {
    public static void main(String[] args) {
        String testStr = "test";
        String testStr2 = "";
        Function<String, Integer> getStringLength = String::length;
        System.out.println(getStringLength.apply(testStr));
        System.out.println(getStringLength.apply(testStr2));
    }
}
