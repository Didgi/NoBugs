package cleancode.patterns.creational.builder.first2;

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
        Shop shop = new Shop();
        System.out.println(shop.createOrder(new String[]{"Молоко", "Курица"}, 50, "Card"));

    }

}
