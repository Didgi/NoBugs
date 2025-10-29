package practice9_multithreading.seventh_task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class UserAccountOperations {

    private final ReentrantLock reentrantLock = new ReentrantLock(true);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public boolean isMoneyEnough(UserAccount accountFrom, int amount) {
        return accountFrom.getBalance() - amount >= 0;
    }

    public void withdrawMoney(UserAccount accountFrom, int amount) {
        reentrantLock.lock();
        try {
            if (isMoneyEnough(accountFrom, amount)) {
                int updatedBalance = accountFrom.getBalance() - amount;
                accountFrom.setBalance(updatedBalance);
                System.out.println("Выполнение withdrawMoney в потоке: " + Thread.currentThread().getName());
            } else {
                System.out.println("Недостаточно средств на счёте: " + accountFrom);
            }
        } finally {
            reentrantLock.unlock();
        }
    }

    public void depositMoney(UserAccount accountTo, int amount) throws InterruptedException {
        reentrantLock.lock();
        try {
            int updatedBalance = accountTo.getBalance() + amount;
            accountTo.setBalance(updatedBalance);
            System.out.println("Выполнение depositMoney в потоке: " + Thread.currentThread().getName());
            Thread.sleep(50); //имитация выполнения операции зачисления. Подумал, что это может
            // дополнительно привести к рассинхрону, когда ещё не было локов, а также проверить, что локи это лечат
        } finally {
            reentrantLock.unlock();
        }
    }

    public void transferMoney(UserAccount accountFrom, UserAccount accountTo, int amount) {
        System.out.println("Баланс на аккаунте: " + accountFrom + " до снятия денег: " + accountFrom.getBalance());
        System.out.println("Сумма перевода: " + amount);

        Runnable transfer = () -> {
            if (isMoneyEnough(accountFrom, amount)) {
                withdrawMoney(accountFrom, amount);
                try {
                    depositMoney(accountTo, amount);
                } catch (InterruptedException e) {
                    System.out.println("Операция выполнения зачисления средств не выполнена. " +
                            "Выполнение программы прервано ошибкой: " + e.getMessage());
                }
            }
        };
        executorService.submit(transfer);
        System.out.println("Баланс на аккаунте: " + accountFrom + " с которого переводили: " + accountFrom.getBalance());
        System.out.println("Баланс на аккаунте: " + accountTo + " на который переводили: " + accountTo.getBalance());

    }

    public void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Потом не завершён за отведённое время. Происходит принудительное прерывание потока.");
            Thread.currentThread().interrupt();
        }
    }

}
