package cleancode.patterns.structural.facade.second;

public class Light implements Turnable{
    @Override
    public void turnOn() {
        System.out.println("Включение света");
    }

    @Override
    public void turnOff() {
        System.out.println("Выключение света");
    }
}
