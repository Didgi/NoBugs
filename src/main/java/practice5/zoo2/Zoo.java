package practice5.zoo2;

public class Zoo {

    private final Animal animal;

    public Zoo(Animal animal) {
        this.animal = animal;
    }

    public void printBehavior() {
        this.animal.makeNoise();
        this.animal.move();
    }

}
