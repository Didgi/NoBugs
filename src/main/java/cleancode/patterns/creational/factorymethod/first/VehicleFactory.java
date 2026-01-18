package cleancode.patterns.creational.factorymethod.first;

abstract class VehicleFactory {
    abstract Moveable createVehicle();

    public void moveVehicle(){
        Moveable moveable = createVehicle();
        moveable.move();
    }
}
