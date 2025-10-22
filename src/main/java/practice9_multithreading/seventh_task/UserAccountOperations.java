package practice9_multithreading.seventh_task;

import practice9_multi_my_practice.task7_new.UserBankAccount;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class UserAccountOperations {

    ReentrantLock reentrantLock = new ReentrantLock(true);

    public boolean isMoneyEnough(UserAccount accountFrom, int amount) {
        return accountFrom.getBalance() - amount >= 0;
    }

    public void withdrawMoney(UserAccount accountFrom, int amount) {
//        reentrantLock.lock();
//        try {
            if (isMoneyEnough(accountFrom, amount)) {
                int updatedBalance = accountFrom.getBalance() - amount;
                accountFrom.setBalance(updatedBalance);
                System.out.println("Выполнение withdrawMoney в потоке: " + Thread.currentThread().getName());
            } else {
                System.out.println("Недостаточно средств на счёте: " + accountFrom);
            }
//        } finally {
//            reentrantLock.unlock();
//        }
    }

    public void depositMoney(UserAccount accountTo, int amount) {
//        reentrantLock.lock();
//        try {
            int updatedBalance = accountTo.getBalance() + amount;
            accountTo.setBalance(updatedBalance);
            System.out.println("Выполнение depositMoney в потоке: " + Thread.currentThread().getName());
//        } finally {
//            reentrantLock.unlock();
//        }
    }
    public void transferMoney(UserAccount accountFrom, UserAccount accountTo, int amount) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        System.out.println("Баланс на аккаунте: " + accountFrom + " до снятия денег: " + accountFrom.getBalance());
        System.out.println("Сумма перевода: " + amount);

        Runnable transfer = () -> {
            if (isMoneyEnough(accountFrom, amount)) {
                withdrawMoney(accountFrom, amount);
                depositMoney(accountTo, amount);
            }
        };
        executorService.execute(transfer);
        try {
            executorService.shutdown();
            executorService.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Баланс на аккаунте: " + accountFrom + " с которого переводили: " + accountFrom.getBalance());
        System.out.println("Баланс на аккаунте: " + accountTo + " на который переводили: " + accountTo.getBalance());

    }
}
