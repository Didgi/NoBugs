package practice5.pet;

public class Cat extends Pet{
    @Override
    public void feed() {
        System.out.println("Кошка ест влажный корм");
    }

    @Override
    public void interact() {
        System.out.println("Кошка играет");
    }
}
