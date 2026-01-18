package cleancode.patterns.creational.abstractfactory.second;

public class ButtonElementMacOs implements IButtonElement {
    @Override
    public void render() {
        System.out.println("Кнопка отрисовалась на Mac OS");
    }
}
