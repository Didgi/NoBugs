package practice5.zoo3;

public class Elephant implements Animal {
    @Override
    public void makeNoise() {
        System.out.println("Слон трубит");
    }

    @Override
    public void move() {
        System.out.println("Слон ходит");
    }
}
