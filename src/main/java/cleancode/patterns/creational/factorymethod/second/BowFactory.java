package cleancode.patterns.creational.factorymethod.second;

public class BowFactory extends WeaponFactory {
    @Override
    IWeapon createWeapon() {
        return new Bow();
    }
}
