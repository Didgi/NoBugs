package cleancode.patterns.creational.abstractfactory.second;

public class WindowElementMacOS implements IWindowElement {
    @Override
    public void render() {
        System.out.println("Окно отрисовалось на Mac OS");
    }
}
