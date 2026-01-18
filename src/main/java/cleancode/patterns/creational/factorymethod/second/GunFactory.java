package cleancode.patterns.creational.factorymethod.second;

public class GunFactory extends WeaponFactory {
    @Override
    IWeapon createWeapon() {
        return new Gun();
    }
}
