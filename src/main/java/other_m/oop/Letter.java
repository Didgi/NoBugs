package other_m.oop;

public class Letter extends Shipment{
    public Letter(double distance, double basePrice) {
        super(distance, basePrice);

    }

    @Override
    public double calculateDelivery(){
        return getDistance() * getBasePrice();
    }

    @Override
    void printInfo() {
        System.out.println("Тип: Письмо. Стоимость доставки: " + calculateDelivery());
//        System.out.printf("Тип: Письмо + " " + . Стоимость доставки: %s%n", calculateDelivery());

    }

    @Override
    public String toString() {
        return "Тип: " +
                "Письмо. " +
                "Стоимость доставки: " + calculateDelivery();
    }
}
