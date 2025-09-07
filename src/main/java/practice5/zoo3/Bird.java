package practice5.zoo3;

public class Bird implements Animal {

    @Override
    public void makeNoise() {
        System.out.println("Птица чирикает");
    }

    @Override
    public void move() {
        System.out.println("Птица летает");
    }
}
