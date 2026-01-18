package cleancode.patterns.creational.factorymethod.first;

public class Main {
    /*
    В этом приложении нам нужно создать систему для создания различных типов транспортных средств
    (например, автомобили и велосипеды).
    Вместо того чтобы код зависел от конкретных классов (например, Car и Bicycle),
    мы будем использовать фабричный метод для создания объектов.
    Это позволит расширять систему, добавляя новые типы транспортных средств,
    не меняя существующий код.
     */
    public static void main(String[] args) {
    VehicleFactory car = new CarFactory();
    car.moveVehicle();

    VehicleFactory bicycle = new BicycleFactory();
    bicycle.moveVehicle();

    }

}
