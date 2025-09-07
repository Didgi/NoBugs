package practice5.pet;

public class Main {
    public static void main(String[] args) {
        Pet cat = new Cat();
        Pet dog = new Dog();
        Shelter shelter = new Shelter();
        shelter.setPet(cat);
        shelter.interact();
        shelter.setPet(dog);
        shelter.interact();
    }
}
