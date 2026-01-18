package cleancode.patterns.creational.abstractfactory.first;

public class ModernFurnitureFactory implements AbstractFactoryStyle{
    @Override
    public IChairProduction createChair() {
        return new ModernChair();
    }

    @Override
    public ITableProduction createTable() {
        return new ModernTable();
    }
}
