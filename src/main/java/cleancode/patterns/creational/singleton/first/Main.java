package cleancode.patterns.creational.singleton.first;

public class Main {
    public static void main(String[] args) {
        ConfigurationManager configurationManagerFirst = ConfigurationManager.getInstance();
        configurationManagerFirst.setUsername("myUsername");
        configurationManagerFirst.setPassword("myPassword");

        System.out.println(configurationManagerFirst);

        ConfigurationManager configurationManagerSecond = ConfigurationManager.getInstance();

        System.out.println(configurationManagerSecond);
    }
}
