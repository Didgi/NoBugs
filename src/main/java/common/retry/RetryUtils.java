package common.retry;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class RetryUtils {

    public static <T> T retry(
            Supplier<T> action,
            Predicate<T> condition,
            int maxRetryAmounts,
            long waitMillis
    ) {
        int amounts = 0;
        while (amounts < maxRetryAmounts){
            amounts++;
            System.out.println("Попытка: " + amounts);
            final T result = action.get();
            if (condition.test(result)){
                return result;
            }
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException("Ожидаемое условие: " + condition + " при выполнении " + action + " не получено");
    }
}
