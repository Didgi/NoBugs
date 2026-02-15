package practice7_exception_generics.generics;

public class PrintArrayMain {

    public static <T> void printArray(T[] array) {
        for (T t : array
        ) {
            System.out.println(t);
        }
    }

    public static void main(String[] args) {
        printArray(new String[]{"First", "2", "String"});
        printArray(new Integer[]{1, 2});
    }
}
