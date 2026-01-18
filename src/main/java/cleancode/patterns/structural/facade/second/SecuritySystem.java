package cleancode.patterns.structural.facade.second;

public class SecuritySystem implements Turnable{
    @Override
    public void turnOn() {
        System.out.println("Включение системы безопасности");
    }

    @Override
    public void turnOff() {
        System.out.println("Выключение системы безопасности");
    }
}
