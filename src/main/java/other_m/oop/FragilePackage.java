package other_m.oop;

public class FragilePackage extends Shipment{

    private final int addFixPrice = 500;
    public FragilePackage(double distance, double basePrice) {
        super(distance, basePrice);

    }

    public double calculateDelivery(){
        return (getDistance() * getBasePrice()) + 500;
    }

    @Override
    void printInfo() {
        System.out.println("Тип: Хрупкий, Handle with care. Стоимость доставки: " + calculateDelivery());
    }

    @Override
    public String toString() {
        return "Тип: " +
                "Хрупкий, Handle with care. " +
                "Стоимость доставки: " + calculateDelivery();
    }
}
