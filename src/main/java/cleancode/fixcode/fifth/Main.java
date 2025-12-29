package cleancode.fixcode.fifth;

public class Main {
    public static void main(String[] args) {
        IPayment creditCard = new CreditCardPayment();
        IPayment payPal = new PayPalPayment();
        IPayment bitcoin = new BitcoinPayment();

        PaymentProcessor paymentProcessor = new PaymentProcessor();
        paymentProcessor.processPayment(creditCard, 50);
        paymentProcessor.processPayment(payPal, 50);
        paymentProcessor.processPayment(bitcoin, 50);
    }
}
