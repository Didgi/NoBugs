package practice8_func_int_lambda_stream_api.part_1;

public class MainAnonymous {
    public static void main(String[] args) {
        Runnable printHello = () -> {
            System.out.println("Hello from anonymous class!");
        };
        printHello.run();
    }
}
