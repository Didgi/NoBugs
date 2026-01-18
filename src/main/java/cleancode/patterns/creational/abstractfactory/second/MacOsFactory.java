package cleancode.patterns.creational.abstractfactory.second;

public class MacOsFactory implements AbstractFactoryElements{
    @Override
    public IButtonElement addButtonElement() {
        return new ButtonElementMacOs();
    }

    @Override
    public IMenuElement addMenuElement() {
        return new MenuElementMacOs();
    }

    @Override
    public IWindowElement addWindowElement() {
        return new WindowElementMacOS();
    }
}
