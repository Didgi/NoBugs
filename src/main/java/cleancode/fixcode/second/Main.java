package cleancode.fixcode.second;

public class Main {
    public static void main(String[] args) {
        DiscountCalculator discountCalculator = new DiscountCalculator();

//        Тест 1: лояльный покупатель с первой покупкой
        double priceForLoyalCustomerWithFirstPurchase = discountCalculator.calculateDiscount(500, true, true, true);
        System.out.println("Цена для лояльного покупателя при первой покупке: "+ priceForLoyalCustomerWithFirstPurchase);

        //Тест 2: лояльный покупатель с не первой покупкой
        double priceForLoyalCustomerWithoutFirstPurchase = discountCalculator.calculateDiscount(500, true, false, true);
        System.out.println("Цена для лояльного покупателя при не первой покупке: "+ priceForLoyalCustomerWithoutFirstPurchase);

        //Тест 3: обычный покупатель с купоном
        double priceForRegularCustomerWithCoupon = discountCalculator.calculateDiscount(500, false, false, true);
        System.out.println("Цена для обычного покупателя с купоном: "+ priceForRegularCustomerWithCoupon);

        //Тест 4: обычный покупатель без купона
        double priceForRegularCustomerWithoutCoupon = discountCalculator.calculateDiscount(500, false, false, false);
        System.out.println("Цена для обычного покупателя без купоном: "+ priceForRegularCustomerWithoutCoupon);

    }
}
