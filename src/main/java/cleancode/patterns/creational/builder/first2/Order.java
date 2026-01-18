package cleancode.patterns.creational.builder.first2;

import java.util.Arrays;

public class Order {
    private String[] goods;
    private int discount;
    private String paymentType;

    public Order(String[] goods, int discount, String paymentType) {
        this.goods = goods;
        this.discount = discount;
        this.paymentType = paymentType;
    }

    public Order(Builder builder) {
        this.goods = builder.goods;
        this.discount = builder.discount;
        this.paymentType = builder.paymentType;
    }

    static class Builder {
        private String[] goods;
        private int discount;
        private String paymentType;

        public Builder setGoods(String[] goods) {
            this.goods = goods;
            return this;
        }

        public Builder setDiscount(int discount) {
            this.discount = discount;
            return this;
        }

        public Builder setPaymentType(String paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Order build() {
            return new Order(this);
        }

    }

    @Override
    public String toString() {
        return "Builder{" +
                "goods=" + Arrays.toString(goods) +
                ", discount=" + discount +
                ", paymentType='" + paymentType + '\'' +
                '}';
    }
}
