package practice9_multithreading.seventh_task;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        UserAccount accountFirst = new UserAccount("Я", 1000);
        UserAccount accountSecond = new UserAccount("Она", 2000);
        UserAccountOperations userAccountOperations = new UserAccountOperations();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int randomAmountMoney = new Random().nextInt(50);
            userAccountOperations.transferMoney(accountFirst, accountSecond, randomAmountMoney);
            userAccountOperations.transferMoney(accountSecond, accountFirst, randomAmountMoney);
        }
        userAccountOperations.shutdown();
        long stopTime = System.currentTimeMillis();
        long resultTime = stopTime - startTime;
        System.out.println("Общее время выполнения: " + resultTime);
        System.out.println("Итоговый баланс аккаунта: '" + accountFirst + "' составляет: " + accountFirst.getBalance());
        System.out.println("Итоговый баланс аккаунта: '" + accountSecond + "' составляет: " + accountSecond.getBalance());
    }
}
