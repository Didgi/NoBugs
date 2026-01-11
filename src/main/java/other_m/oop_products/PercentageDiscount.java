package other_m.oop_products;

public class PercentageDiscount implements Discountable{
    private int discount;
    public PercentageDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public double percentageDiscount(double price) {
        return price * discount / 100;
    }

    @Override
    public double fixedDiscount(double price) {
        return price;
    }
}
