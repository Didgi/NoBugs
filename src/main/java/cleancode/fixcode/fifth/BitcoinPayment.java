package cleancode.fixcode.fifth;

public class BitcoinPayment implements IPayment {
    @Override
    public void paymentProcessor(double amount) {
        System.out.println("Оплата Bitcoin на сумму " + amount);
    }
}
