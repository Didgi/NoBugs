package practice_2;

public class Laptop {

    String brand;
    double price;

    Laptop(String brand, double price){
        this.brand = brand;
        this.price = price;
    }

    public String getBrand(){
        return this.brand;
    }

    public double getPrice(){
        return this.price;
    }

    public void setBrand(String newBrand){
        this.brand = newBrand;
    }

    public void setPrice(double newPrice){
        this.price = newPrice;
    }

    public void printInfo(){
        System.out.println("Стоимость MacBook от компании " + this.brand + " составляет " + this.price);
    }
}
