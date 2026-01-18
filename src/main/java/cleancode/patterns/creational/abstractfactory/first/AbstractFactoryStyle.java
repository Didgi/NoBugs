package cleancode.patterns.creational.abstractfactory.first;

public interface AbstractFactoryStyle {
    IChairProduction createChair();
    ITableProduction createTable();
}
