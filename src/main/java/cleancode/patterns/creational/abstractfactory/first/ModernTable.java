package cleancode.patterns.creational.abstractfactory.first;

public class ModernTable implements ITableProduction {
    @Override
    public void show() {
        System.out.println("Стол в современном стиле");
    }
}
