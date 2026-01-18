package cleancode.patterns.creational.factorymethod.second;

public class SwordFactory extends WeaponFactory {
    @Override
    IWeapon createWeapon() {
        return new Sword();
    }
}
