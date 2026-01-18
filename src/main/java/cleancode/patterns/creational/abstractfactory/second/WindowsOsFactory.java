package cleancode.patterns.creational.abstractfactory.second;

public class WindowsOsFactory implements AbstractFactoryElements{
    @Override
    public IButtonElement addButtonElement() {
        return new ButtonElementWindowsOS();
    }

    @Override
    public IMenuElement addMenuElement() {
        return new MenuElementWindowsOs();
    }

    @Override
    public IWindowElement addWindowElement() {
        return new WindowElementWindowsOS();
    }
}
