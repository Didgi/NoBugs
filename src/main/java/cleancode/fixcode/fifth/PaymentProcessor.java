package cleancode.fixcode.fifth;

public class PaymentProcessor {
    public void processPayment(Pay paymentType, double amount) {
        paymentType.paymentProcessor(amount);
    }
}
