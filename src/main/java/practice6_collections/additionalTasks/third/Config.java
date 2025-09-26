package practice6_collections.additionalTasks.third;

import java.util.LinkedHashMap;

public class Config {

    private LinkedHashMap<ParamName, String> config;

    public LinkedHashMap<ParamName, String> getConfig() {
        return config;
    }

    public Config() {
        config = new LinkedHashMap<>();
    }

    public void addParamToConfig(ParamName paramName, String paramValue) {
        config.put(paramName, paramValue);
    }
}
