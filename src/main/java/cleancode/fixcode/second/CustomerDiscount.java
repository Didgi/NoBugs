package cleancode.fixcode.second;

public class CustomerDiscount implements Discountable {

    private CustomerPaymentsService customerPaymentsService;

    public CustomerDiscount(CustomerPaymentsService customerPaymentsService) {
        this.customerPaymentsService = customerPaymentsService;
    }

    private final double discount_on_first_purchase_loyal_customer = 0.10;
    private static final double discount_on_other_purchases_loyal_customer = 0.05;
    private static final double discount_on_purchase_with_coupon = 0.07;
    private static final double discount_on_purchase_without_coupon = 0.02;

    public double applyDiscount(double price, Customer customer) {
        if (customer.isLoyal()) {
            if (customerPaymentsService.isFirstPurchaseCustomer(customer)) {
                System.out.println("Применена скидка " + discount_on_first_purchase_loyal_customer + " на 1ю покупку");
                return price * discount_on_first_purchase_loyal_customer;
            } else {
                System.out.println("Применена скидка " + discount_on_other_purchases_loyal_customer + " на покупку");
                return price * discount_on_other_purchases_loyal_customer;
            }
        } else {
            if (customer.isHasCoupon()) {
                System.out.println("Применена скидка " + discount_on_purchase_with_coupon + " на покупку с купоном");
                return price * discount_on_purchase_with_coupon;
            } else {
                System.out.println("Применена скидка " + discount_on_purchase_without_coupon + " на покупку без купона");
                return price * discount_on_purchase_without_coupon;
            }
        }
    }
}
