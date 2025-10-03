package practice7_exception_generics.generics;

public class BoxMain {
    public static void main(String[] args) {
        Box<Integer> box = new Box<>();
        box.setValue(5);
        int value = box.getValue();
        System.out.println(value);

        Box<String> box1 = new Box<>();
        box1.setValue("First");
        System.out.println(box1.getValue());
    }
}
