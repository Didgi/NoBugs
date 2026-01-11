package other_m.oop;

public class BasicPackage extends Shipment{

    private final int addFixPrice = 100;
    public BasicPackage(double distance, double basePrice) {
        super(distance, basePrice);

    }

    public double calculateDelivery(){
        return (getDistance() * getBasePrice()) + 100;
    }

    @Override
    void printInfo() {
        System.out.println("Тип: Посылка. Стоимость доставки: " + calculateDelivery());
    }

    @Override
    public String toString() {
        return "Тип: " +
                "Посылка. " +
                "Стоимость доставки: " + calculateDelivery();
    }
}
