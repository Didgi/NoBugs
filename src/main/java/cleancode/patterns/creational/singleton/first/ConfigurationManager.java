package cleancode.patterns.creational.singleton.first;

public class ConfigurationManager {

    private static ConfigurationManager configurationManager;
    /*
    Параметры подключения к базе данных (например, URL базы данных, имя пользователя, пароль).
Путь к папке для хранения файлов.
Настройки логирования (например, уровень логирования, путь к лог-файлу).
     */
    private String dbUrl;
    private String username;
    private String password;
    private String storagePath;
    private String levelLog;
    private String pathLog;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance(){
        if (configurationManager == null){
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setLevelLog(String levelLog) {
        this.levelLog = levelLog;
    }

    public void setPathLog(String pathLog) {
        this.pathLog = pathLog;
    }

    @Override
    public String toString() {
        return "ConfigurationManager params: " +
                "dbUrl='" + dbUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", storagePath='" + storagePath + '\'' +
                ", levelLog='" + levelLog + '\'' +
                ", pathLog='" + pathLog + '\'';
    }
}
