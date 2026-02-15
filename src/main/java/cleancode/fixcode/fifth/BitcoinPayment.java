package cleancode.fixcode.fifth;

public class BitcoinPayment implements Pay {
    @Override
    public void paymentProcessor(double amount) {
        System.out.println("Оплата Bitcoin на сумму " + amount);
    }
}
