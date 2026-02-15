package practice5.zoo;

public class Main {
    public static void main(String[] args) {
        Animal elephant = new Elephant();
        Animal bird = new Bird();
        Zoo zoo = new Zoo();
        zoo.setAnimal(elephant);
        zoo.printBehavior();
        zoo.setAnimal(bird);
        zoo.printBehavior();
    }
}
