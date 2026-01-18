package cleancode.patterns.creational.abstractfactory.first;

public class ClassicTable implements ITableProduction {
    @Override
    public void show() {
        System.out.println("Стол в классическом стиле");
    }
}
