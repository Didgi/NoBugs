package practice5.amusement_park;

public class Main {
    public static void main(String[] args) {
        Park park = new Park();
        Attraction rollerCoaster = new RollerCoaster();
        Attraction carousel = new Carousel();
        park.showAttractionDescription(rollerCoaster);
        park.manageAttraction(rollerCoaster);

        park.showAttractionDescription(carousel);
        park.manageAttraction(carousel);
    }
}
