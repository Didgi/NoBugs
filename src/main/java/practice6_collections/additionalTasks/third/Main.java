package practice6_collections.additionalTasks.third;

public class Main {
    public static void main(String[] args) {
        Config config = new Config();
        config.addParamToConfig(ParamName.ENV, "testEnv");
        config.addParamToConfig(ParamName.DATABASE, "hostDb");
        System.out.println(config.getConfig());
    }

}
