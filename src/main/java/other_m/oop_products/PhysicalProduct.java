package other_m.oop_products;

public class PhysicalProduct extends Product {

    private final double weight;

    public PhysicalProduct(String title, double price, double weight) {
        super(title, price);
        this.weight = weight;
    }
}
