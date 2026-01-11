package other_m;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

public class SiteManager {
    Map<Users, LocalDateTime> siteTimeVisitMap = new HashMap<>();
    List<Users> siteVisitors = new ArrayList<>();

    public void addVisitors2(Users users){
        siteVisitors.add(users);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        final String formatDate = simpleDateFormat.format(date);
//        Date timeVisit = new Date();
//        String time = new Date().getTime()
//        siteTimeVisitMap.put(users, formatDate);
        System.out.println("На сайт зашёл пользователь: " + users + " в " + formatDate);
    }

    public void addVisitors(Users users){
        LocalDateTime date = LocalDateTime.now();
        siteVisitors.add(users);
        siteTimeVisitMap.put(users, date);
        System.out.println("На сайт зашёл пользователь: " + users + " в " + date);
    }

    public void getSiteVisitors(){
        for (Users siteVisitor : siteVisitors) {
            System.out.println("Статистика посещений!");
            System.out.println("На сайт заходил пользователь: " + siteVisitor + " в " + siteTimeVisitMap.get(siteVisitor));
        }
    }

}
