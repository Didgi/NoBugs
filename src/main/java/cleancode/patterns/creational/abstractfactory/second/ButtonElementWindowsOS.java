package cleancode.patterns.creational.abstractfactory.second;

public class ButtonElementWindowsOS implements IButtonElement {
    @Override
    public void render() {
        System.out.println("Кнопка отрисовалась на Windows OS");
    }
}
