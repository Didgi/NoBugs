package cleancode.patterns.creational.abstractfactory.second;

public class MenuElementMacOs implements IMenuElement {
    @Override
    public void render() {
        System.out.println("Меню отрисовалось на Mac OS");
    }
}
