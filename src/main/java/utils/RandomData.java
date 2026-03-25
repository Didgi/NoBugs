package utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;


public class RandomData {
    private RandomData() {
    }

    public static Double getMoneyFromTo(int minBound, int maxBound) {
        final double randomValue = ThreadLocalRandom.current().nextDouble(minBound, maxBound);
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double getMoney() {
        final double randomValue = ThreadLocalRandom.current().nextDouble(0.01, 5000);
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

}
