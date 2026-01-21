package cleancode.patterns.creational.singleton.second2;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getInstance();
        logger.addLogEvent("Db connection is success");
        System.out.println(logger);
        Logger loggerSecond = Logger.getInstance();
        System.out.println(loggerSecond);
    }
}
