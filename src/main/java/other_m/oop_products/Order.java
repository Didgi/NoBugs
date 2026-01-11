package other_m.oop_products;

import java.util.LinkedList;
import java.util.List;

public class Order {

    List<Product> products;
    List<Discountable> discounts;
    public Order() {
        products = new LinkedList<>();
    }

    public void addProduct(Product product){
        products.add(product);
    }

    public void addDiscount(Discountable discountable){
        discounts.add(discountable);
    }

    public double getTotalOrderPrice(){
      return products.stream().mapToDouble(Product::getPrice).sum();
    }
}
