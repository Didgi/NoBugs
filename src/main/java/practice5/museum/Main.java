package practice5.museum;

public class Main {
    public static void main(String[] args) {
        Museum museum = new Museum();
        Exhibit manuscript = new Manuscript("Манускрипт написан в 2 веке до нашей эры");
        Exhibit sculpture = new Sculpture("Скульптура создана в римской империи");
        museum.getExhibitHistory(manuscript);
        museum.getExhibitConditionStorage(manuscript);
        museum.getExhibitHistory(sculpture);
        museum.getExhibitConditionStorage(sculpture);

    }
}
