package cleancode.fixcode.fifth;

public class CreditCardPayment implements Pay {
    @Override
    public void paymentProcessor(double amount) {
        System.out.println("Оплата кредитной картой на сумму " + amount);
    }
}
