package practice6_collections.additionalTasks.fourth;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class WebsiteTracking {


    private LinkedList<String> visitHistory = new LinkedList<>();
    private LinkedHashSet<String> uniqWebsite;
    private int totalCountVisit;

    public LinkedList<String> getVisitHistory() {
        return visitHistory;
    }

    public int getTotalCountVisit() {
        return totalCountVisit;
    }

    public LinkedHashSet<String> getUniqWebsite() {
        return uniqWebsite;
    }

    public WebsiteTracking() {
        uniqWebsite = new LinkedHashSet<>();

    }

    public void openWebsite(String url) {
        visitHistory.add(url.toLowerCase());
        uniqWebsite.add(url.toLowerCase());
        totalCountVisit++;
    }

}
