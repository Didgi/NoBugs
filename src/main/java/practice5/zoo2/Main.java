package practice5.zoo2;

public class Main {
    public static void main(String[] args) {
        Animal elephant = new Elephant();
        Animal bird = new Bird();
        Zoo zooElephant = new Zoo(elephant);
        Zoo zooBird = new Zoo(bird);
        zooElephant.printBehavior();
        zooBird.printBehavior();
    }
}
