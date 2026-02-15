package cleancode.fixcode.second;

public class DiscountCalculator {

    private static final double DISCOUNT_ON_FIRST_PURCHASE_LOYAL_CUSTOMER = 0.10;
    private static final double DISCOUNT_ON_OTHER_PURCHASES_LOYAL_CUSTOMER = 0.05;
    private static final double DISCOUNT_ON_PURCHASE_WITH_COUPON = 0.07;
    private static final double DISCOUNT_ON_PURCHASE_WITHOUT_COUPON = 0.02;

    public double calculateDiscount(double price, boolean isLoyalCustomer, boolean isFirstPurchase, boolean hasCoupon) {
        double discountRate = getDiscountRate(isLoyalCustomer, isFirstPurchase, hasCoupon);
        return price - (price * discountRate);
    }

    public double getDiscountRate(boolean isLoyalCustomer, boolean isFirstPurchase, boolean hasCoupon) {
        if (isLoyalCustomer) {
            return isFirstPurchase ? DISCOUNT_ON_FIRST_PURCHASE_LOYAL_CUSTOMER : DISCOUNT_ON_OTHER_PURCHASES_LOYAL_CUSTOMER;
        } else {
            return hasCoupon ? DISCOUNT_ON_PURCHASE_WITH_COUPON : DISCOUNT_ON_PURCHASE_WITHOUT_COUPON;
        }
    }
}
//Задача: Упростите код, убрав вложенные условия, сделав его более читаемым и поддерживаемым.
