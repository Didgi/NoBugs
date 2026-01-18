package cleancode.patterns.creational.abstractfactory.second;

public class WindowElementWindowsOS implements IWindowElement {
    @Override
    public void render() {
        System.out.println("Окно отрисовалось на Windows OS");
    }
}
