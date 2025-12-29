package cleancode.fixcode.second;

public class DiscountCalculator {
    private Discountable customerDiscount;

    public DiscountCalculator(CustomerPaymentsService customerPaymentsService) {
        this.customerDiscount = new CustomerDiscount(customerPaymentsService);
    }

    public double calculateDiscount(double price, Customer customer) {
        double discountValue = customerDiscount.applyDiscount(price, customer);
        return price - discountValue;
    }
}
//Задача: Упростите код, убрав вложенные условия, сделав его более читаемым и поддерживаемым.
