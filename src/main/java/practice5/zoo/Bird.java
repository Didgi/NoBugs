package practice5.zoo;

public class Bird extends Animal {

    @Override
    public void makeNoise() {
        System.out.println("Птица чирикает");
    }

    @Override
    public void move() {
        System.out.println("Птица летает");
    }
}
