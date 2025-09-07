package practice5.amusement_park;

public class Park {

    public void manageAttraction(Attraction attraction){
        attraction.maintenance();
    }
    public void showAttractionDescription(Attraction attraction){
        attraction.getDescription();
    }
}
