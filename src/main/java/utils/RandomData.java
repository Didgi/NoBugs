package utils;

import org.apache.commons.text.RandomStringGenerator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;


public class RandomData {
    private RandomData() {
    }

    public static String getUsername() {
        return RandomStringGenerator.builder().withinRange('a', 'z').get().generate(3)
                + RandomStringGenerator.builder().withinRange('A', 'Z').get().generate(3);

    }

    public static String getPassword() {
        return RandomStringGenerator.builder().withinRange('a', 'z').get().generate(3)
                + RandomStringGenerator.builder().withinRange('A', 'Z').get().generate(3)
                + RandomStringGenerator.builder().withinRange('0', '9').get().generate(3)
                + RandomStringGenerator.builder().selectFrom(new char[]{'!', '%', '^', '@'}).get().generate(2);
    }

    public static Double getMoneyFromTo(int minBound, int maxBound) {
        final double randomValue = ThreadLocalRandom.current().nextDouble(minBound, maxBound);
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static Double getMoney() {
        final double randomValue = ThreadLocalRandom.current().nextDouble(0.01, 5000);
        return BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public static String getName() {
        return RandomStringGenerator.builder().withinRange('a', 'z').get().generate(5) + ' '
                + RandomStringGenerator.builder().withinRange('A', 'Z').get().generate(5);
    }

}
