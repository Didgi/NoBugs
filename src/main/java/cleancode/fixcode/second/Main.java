package cleancode.fixcode.second;

public class Main {
    public static void main(String[] args) {
        CustomerPaymentsService customerPaymentsService = new CustomerPaymentsService();
        DiscountCalculator discountCalculator = new DiscountCalculator(customerPaymentsService);

//        Тест 1: лояльный покупатель без покупок
        Customer loyalCustomerFirstPurchase = new Customer(true, true);
        System.out.println(discountCalculator.calculateDiscount(500, loyalCustomerFirstPurchase));

        //Тест 2: лояльный покупатель с не первой покупкой
        Customer loyalCustomerWithSomePurchases = new Customer(true, true);
        customerPaymentsService.addCustomerPayment(loyalCustomerWithSomePurchases);
        System.out.println(discountCalculator.calculateDiscount(500, loyalCustomerWithSomePurchases));

        //Тест 3: обычный покупатель с купоном
        Customer usualCustomerWithCoupon = new Customer(false, true);
        customerPaymentsService.addCustomerPayment(usualCustomerWithCoupon);
        System.out.println(discountCalculator.calculateDiscount(500, usualCustomerWithCoupon));

        //Тест 4: обычный покупатель без купона
        Customer usualCustomerWithoutCoupon = new Customer(false, true);
        customerPaymentsService.addCustomerPayment(usualCustomerWithoutCoupon);
        System.out.println(discountCalculator.calculateDiscount(500, usualCustomerWithoutCoupon));

    }
}
