package cleancode.patterns.creational.builder.first2;

public class Shop {
    public Order createOrder(String[] goods, int discount, String paymentType){
        return new Order.Builder()
                .setGoods(goods)
                .setDiscount(discount)
                .setPaymentType(paymentType)
                .build();
    }
}
