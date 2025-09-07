package practice5.zoo3;

public class Zoo {

    private final Animal animal;

    public Zoo(Animal animalInt) {
        this.animal = animalInt;
    }

    public void printBehavior() {
        this.animal.makeNoise();
        this.animal.move();
    }

}
