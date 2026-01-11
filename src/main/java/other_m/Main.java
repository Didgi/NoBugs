package other_m;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Users userAnon = new AnonymousUsers("Анон");
        Users userAuth = new AuthorizedUsers("Авторизованный");
        Users userCoop = new CoopUser("Тестовая", "Корпорат");

        SiteManager siteManager = new SiteManager();
        siteManager.addVisitors(userAnon);
        Thread.sleep(2000);
        siteManager.addVisitors(userAuth);
        Thread.sleep(2000);
        siteManager.addVisitors(userCoop);
        Thread.sleep(2000);
        siteManager.addVisitors(userAnon);
        siteManager.getSiteVisitors();

    }
}
