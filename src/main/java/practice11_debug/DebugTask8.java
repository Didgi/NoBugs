package practice11_debug;

import java.math.BigDecimal;

public class DebugTask8 {
    public static void main(String[] args) {
        BigDecimal a = BigDecimal.valueOf(0.1).multiply(BigDecimal.valueOf(3));
        BigDecimal b = BigDecimal.valueOf(0.3);
        if (a.equals(b)) {
            System.out.println("Equal");
        } else {
            System.out.println("Not Equal");
        }
    }
}
