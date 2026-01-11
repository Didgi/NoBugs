package other_m.oop;

public abstract class Shipment {
    protected double distance;
    protected double basePrice;

    public Shipment(double distance, double basePrice) {
        this.distance = distance;
        this.basePrice = basePrice;
    }

    public double getDistance() {
        return distance;
    }

    public double getBasePrice() {
        return basePrice;
    }

    abstract double calculateDelivery();
    abstract void printInfo();
}
