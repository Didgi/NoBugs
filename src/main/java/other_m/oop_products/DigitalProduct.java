package other_m.oop_products;

public class DigitalProduct extends Product {
    private final double fileSize;

    public DigitalProduct(String title, double price, double fileSize) {
        super(title, price);
        this.fileSize = fileSize;
    }
}
