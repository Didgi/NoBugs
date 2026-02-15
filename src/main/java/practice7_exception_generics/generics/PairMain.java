package practice7_exception_generics.generics;

public class PairMain {
    public static void main(String[] args) {
        Pair<String, Number> pair = new Pair<>();
        pair.setValueK("First");
        pair.setValueV(2.5);
        System.out.println(pair.getValueK());
        System.out.println(pair.getValueV());
    }
}
