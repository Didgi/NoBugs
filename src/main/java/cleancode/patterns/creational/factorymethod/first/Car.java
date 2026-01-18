package cleancode.patterns.creational.factorymethod.first;

public class Car implements Moveable {
    @Override
    public void move() {
        System.out.println("Машина едет быстро");
    }
}
