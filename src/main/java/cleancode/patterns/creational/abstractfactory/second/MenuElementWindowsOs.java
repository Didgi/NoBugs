package cleancode.patterns.creational.abstractfactory.second;

public class MenuElementWindowsOs implements IMenuElement {
    @Override
    public void render() {
        System.out.println("Меню отрисовалось на Windows OS");
    }
}
