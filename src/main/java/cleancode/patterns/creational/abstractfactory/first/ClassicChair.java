package cleancode.patterns.creational.abstractfactory.first;

public class ClassicChair implements IChairProduction {
    @Override
    public void show() {
        System.out.println("Кресло в классическом стиле");
    }
}
