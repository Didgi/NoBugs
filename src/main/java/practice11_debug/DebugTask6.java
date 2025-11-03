package practice11_debug;

public class DebugTask6 {
    public static void main(String[] args) {
        countdown(5);
    }

    public static void countdown(int n) {
        System.out.println(n);
        if (n > 1) {
            countdown(n - 1);
        }
    }
}
