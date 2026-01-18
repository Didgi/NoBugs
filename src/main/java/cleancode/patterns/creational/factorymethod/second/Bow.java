package cleancode.patterns.creational.factorymethod.second;

public class Bow implements IWeapon {
    @Override
    public void damage() {
        System.out.println("Лук стреляет точно");
    }
}
