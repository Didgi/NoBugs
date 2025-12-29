package cleancode.fixcode.second;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private static int ids = 1;
    private int id = ids++;
    private boolean isLoyal;
    private boolean hasCoupon;

    public Customer(boolean isLoyal, boolean hasCoupon) {
        this.isLoyal = isLoyal;
        this.hasCoupon = hasCoupon;
    }
}
