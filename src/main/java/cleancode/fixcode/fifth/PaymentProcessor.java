package cleancode.fixcode.fifth;

public class PaymentProcessor {
    public void processPayment(IPayment paymentType, double amount) {
        paymentType.paymentProcessor(amount);
    }
}
