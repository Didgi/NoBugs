package other_m;

public interface Moveable {
    int value = 0;
    default void makeV(){
        System.out.println(value);
    }
}
