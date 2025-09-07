package practice5.amusement_park;

public class Carousel extends Attraction {


    @Override
    public void getDescription() {
        System.out.println("Карусель - плавная езда");
    }

    @Override
    public void maintenance() {
        System.out.println("Карусель требует технического обслуживания");
    }
}
