package practice_2;

public class Product {

    String name;
    double price;

    Product(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public void applyDiscount(double discount){
        if (discount > 0 && discount <= 100) {
            this.price = this.price - (this.price * discount / 100);
        }
        else {
            System.out.println("Передано некорректное значение скидки " + discount);
        }
    }

    public void printInfo(){
        System.out.println("Товар " + this.name + " имеет следующую стоимость " + this.price);
    }
}
