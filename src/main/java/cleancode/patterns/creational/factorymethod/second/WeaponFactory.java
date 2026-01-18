package cleancode.patterns.creational.factorymethod.second;

abstract class WeaponFactory {
    abstract IWeapon createWeapon();

    public void damage(){
        IWeapon IWeapon = createWeapon();
        IWeapon.damage();
    }
}
