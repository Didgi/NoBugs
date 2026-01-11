package other_m.oop_products;

public class FixedDiscount implements Discountable{

    private int discount;

    @Override
    public double percentageDiscount(double price) {
        return price;
    }

    @Override
    public double fixedDiscount(double price) {
        return price - discount;
    }
}
