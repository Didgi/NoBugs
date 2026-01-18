package cleancode.patterns.creational.factorymethod.first;

public class CarFactory extends VehicleFactory{
    @Override
    Moveable createVehicle() {
        return new Car();
    }
}
