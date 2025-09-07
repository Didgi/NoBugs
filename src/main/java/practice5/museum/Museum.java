package practice5.museum;

public class Museum {

    public void getExhibitHistory(Exhibit exhibit) {
        String exhibitHistory = exhibit.getHistory();
        System.out.println(exhibitHistory);
    }

    public void getExhibitConditionStorage(Exhibit exhibit) {
        exhibit.getConditionStorage();
    }
}
