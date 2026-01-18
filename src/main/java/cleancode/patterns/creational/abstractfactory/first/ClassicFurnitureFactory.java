package cleancode.patterns.creational.abstractfactory.first;

public class ClassicFurnitureFactory implements AbstractFactoryStyle{
    @Override
    public IChairProduction createChair() {
        return new ClassicChair();
    }

    @Override
    public ITableProduction createTable() {
        return new ClassicTable();
    }
}
