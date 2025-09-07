package practice5.zoo;

public class Zoo {

    private Animal animal;

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public void printBehavior() {
        this.animal.makeNoise();
        this.animal.move();
    }

}
