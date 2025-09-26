package practice6_collections.additionalTasks.fourth;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        WebsiteTracking websiteTracking = new WebsiteTracking();
        websiteTracking.openWebsite("NoBugs");
        websiteTracking.openWebsite("NoBugs");
        websiteTracking.openWebsite("Youtube");
        websiteTracking.openWebsite("NoBugs");
        System.out.println("Cписок уникальных сайтов: " + websiteTracking.getUniqWebsite());
        System.out.println("Общее количество посещений " + websiteTracking.getTotalCountVisit());
        System.out.println("История посещений сайтов " + websiteTracking.getVisitHistory());
    }
}
