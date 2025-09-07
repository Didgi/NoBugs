package practice5.restaurant;

public class HotDish extends Dish {

    private double temperature;


    public HotDish(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public void printDescription() {
        System.out.println("Горячее блюдо с температурой: " + this.temperature);
    }
}
