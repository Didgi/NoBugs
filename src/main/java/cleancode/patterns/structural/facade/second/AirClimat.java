package cleancode.patterns.structural.facade.second;

public class AirClimat implements Turnable{
    @Override
    public void turnOn() {
        System.out.println("Включение кондиционера");
    }

    @Override
    public void turnOff() {
        System.out.println("Выключение кондиционера");
    }
}
