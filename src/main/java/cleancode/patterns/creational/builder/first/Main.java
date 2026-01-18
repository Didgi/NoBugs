package cleancode.patterns.creational.builder.first;

public class Main {
    /*
    В интернет-магазине клиент может оформить заказ, добавляя товары,
    указывая скидку и выбирая способ оплаты.
    Паттерн Builder поможет организовать процесс формирования заказа,
    не перегружая конструктор класса Order множеством параметров.
    Это позволяет строить объекты заказов поэтапно, добавляя товары,
    скидки и способы оплаты по мере необходимости.
     */
    public static void main(String[] args) {

        Order orderFirst = new Order.Builder()
                .setGoods(new String[]{"Молоко", "Курица"})
                .setDiscount(50)
                .setPaymentType("Card")
                .build();

        System.out.println(orderFirst);

        Order orderSecond = new Order.Builder()
                .setGoods(new String[]{"Сиги"})
                .build();
        System.out.println(orderSecond);

    }

}
