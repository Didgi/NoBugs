package practice5.restaurant;

public class Beverage extends Dish {

    private int volume;

    public Beverage(int volume) {
        this.volume = volume;
    }


    @Override
    public void printDescription() {
        System.out.println("Напиток с объёмом " + this.volume);
    }
}
