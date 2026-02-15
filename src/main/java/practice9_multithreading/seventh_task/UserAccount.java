package practice9_multithreading.seventh_task;

public class UserAccount {
    private volatile int balance;
    private final String accountName;

    public UserAccount(String accountName, int balance){
        this.accountName = accountName;
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getAccountName() {
        return accountName;
    }

    @Override
    public String toString(){
        return accountName;
    }

}
