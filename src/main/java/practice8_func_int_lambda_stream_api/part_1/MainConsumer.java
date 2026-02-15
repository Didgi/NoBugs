package practice8_func_int_lambda_stream_api.part_1;

import java.util.function.Consumer;

public class MainConsumer {
    public static void main(String[] args) {
        Consumer<String> printString = System.out::println;
        printString.accept("Hello, guys!");
    }

}
