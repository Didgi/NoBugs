package other_m.oop_products;

public class Main {
    public static void main(String[] args) {
        Order order = new Order();
        order.addProduct(new DigitalProduct("Книга", 50, 3));
        order.addProduct(new PhysicalProduct("Книга", 100, 1));
        PercentageDiscount percentageDiscount = new PercentageDiscount(100);
        System.out.println(order.getTotalOrderPrice());
        System.out.println(percentageDiscount.percentageDiscount(order.getTotalOrderPrice()));
    }
}
