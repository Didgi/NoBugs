package cleancode.patterns.creational.abstractfactory.second;

public class Main {
    /*
    Приложение должно работать одинаково на разных платформах (Windows и MacOS),
    но выглядеть по-разному, соответственно стилям каждой платформы.
    Мы будем использовать абстрактную фабрику для создания различных графических элементов
    (например, кнопок, окон и меню), которые будут выглядеть по-разному в зависимости от платформы.
     */
    public static void main(String[] args) {
        AbstractFactoryElements windowsOsElements = new WindowsOsFactory();
        windowsOsElements.addButtonElement().render();
        windowsOsElements.addMenuElement().render();
        windowsOsElements.addWindowElement().render();

        AbstractFactoryElements macOsElements = new MacOsFactory();
        macOsElements.addWindowElement().render();
    }
}
