package cleancode.fixcode.fifth;

public class PayPalPayment implements Pay {
    @Override
    public void paymentProcessor(double amount) {
        System.out.println("Оплата через PayPal на сумму " + amount);
    }
}
