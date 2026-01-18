package cleancode.patterns.creational.factorymethod.first;

public class BicycleFactory extends VehicleFactory{
    @Override
    Moveable createVehicle() {
        return new Bicycle();
    }
}
