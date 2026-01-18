package cleancode.patterns.creational.factorymethod.second;

public class Gun implements IWeapon {
    @Override
    public void damage() {
        System.out.println("Пистолет стреляет громко");
    }
}
