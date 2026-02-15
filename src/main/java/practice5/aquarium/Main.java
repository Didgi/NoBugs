package practice5.aquarium;

public class Main {

    public static void main(String[] args) {
        Aquarium aquarium = new Aquarium();
        SeaEntity shark = new Shark();
        SeaEntity seaStar = new SeaStar();
        aquarium.setSeaEntity(shark);
        aquarium.showBehavior();
        aquarium.setSeaEntity(seaStar);
        aquarium.showBehavior();
    }
}
