package practice_2;

public class BankAccount {
    String owner;
    double balance;

    BankAccount(String owner, double balance){
        this.owner = owner;
        this.balance = balance;
    }

    public String getOwner(){
        return this.owner;
    }

    public double getBalance(){
        return this.balance;
    }

    public void setOwner(String newOwner){
        this.owner = newOwner;
    }

    public void deposit(double amount){
        if (amount > 0) {
            this.balance += amount;
        }
        else {
            System.out.println("Передано некорректное значение " + amount);
        }
    }

    public void withdraw(double amount){
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
        }
        else {
            System.out.println("Передано некорректное значение " + amount);
        }
    }

    public void printBalance(){
        System.out.println("Владелец " + this.owner + " имеет баланс " + this.balance);
    }
}
