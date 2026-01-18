package cleancode.patterns.creational.factorymethod.first;

public class Bicycle implements Moveable {
    @Override
    public void move() {
        System.out.println("Велосипед движется быстро");
    }
}
