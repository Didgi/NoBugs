package cleancode.fixcode.second;

import java.util.HashMap;
import java.util.Map;

public class CustomerPaymentsService {

    private Map<Customer, Integer> customerCountPurchases = new HashMap<>();

    public boolean isFirstPurchaseCustomer(Customer customer) {
        for (Map.Entry<Customer, Integer> map : customerCountPurchases.entrySet()) {
            if (map.getKey().getId() == customer.getId() && map.getValue() > 0) {
                return false;
            }
        }
        return true;
    }

    public void addCustomerPayment(Customer customer) {
        System.out.println("Совершена покупка покупателем под номером: " + customer.getId());
        final int defaultValue = customerCountPurchases.getOrDefault(customer, 0);
        customerCountPurchases.put(customer, defaultValue + 1);
        System.out.println("Итоговое количество покупок покупателя под номером: "
                + customer.getId() + " - " + customerCountPurchases.get(customer));
    }
}
