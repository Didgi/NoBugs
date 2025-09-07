package practice5.garden;

public class Main {
    public static void main(String[] args) {

        Garden garden = new Garden();
        Plant orchid = new Orchid();
        Plant cactus = new Cactus();
        garden.setPlant(orchid);
        garden.care();
        garden.setPlant(cactus);
        garden.care();

    }
}
