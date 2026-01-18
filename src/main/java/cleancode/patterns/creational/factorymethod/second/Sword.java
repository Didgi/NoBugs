package cleancode.patterns.creational.factorymethod.second;

public class Sword implements IWeapon {
    @Override
    public void damage() {
        System.out.println("Меч колит и рубит");
    }
}
