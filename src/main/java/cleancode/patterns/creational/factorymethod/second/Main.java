package cleancode.patterns.creational.factorymethod.second;

public class Main {
    /*
    В этой задаче нам нужно создать систему для создания различных видов оружия в игре
    (например, мечи, луки и пистолеты).
    Все оружие будет иметь общий интерфейс, но конкретные реализации оружия будут различаться.
    Мы будем использовать фабричный метод, чтобы централизованно создавать объекты оружия.
     */
    public static void main(String[] args) {
    WeaponFactory gun = new GunFactory();
    gun.damage();

    WeaponFactory sword = new SwordFactory();
    sword.damage();

    }

}
