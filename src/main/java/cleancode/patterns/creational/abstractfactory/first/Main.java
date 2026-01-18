package cleancode.patterns.creational.abstractfactory.first;

public class Main {
    /*
    В мебельном магазине предлагаются два стиля мебели: современный и классический.
    Клиент должен выбрать только стиль, а конкретные элементы мебели (например, стулья и столы)
    должны подбираться автоматически в зависимости от выбранного стиля.
    Мы будем использовать абстрактную фабрику, чтобы разделить создание современных и
    классических элементов мебели.
     */
    public static void main(String[] args) {
        AbstractFactoryStyle classicFurniture = new ClassicFurnitureFactory();
        classicFurniture.createChair().show();
        classicFurniture.createTable().show();

        AbstractFactoryStyle modernFurniture = new ModernFurnitureFactory();
        modernFurniture.createChair().show();
        modernFurniture.createTable().show();
    }
}
