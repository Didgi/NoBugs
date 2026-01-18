package cleancode.patterns.creational.singleton.second;

import java.util.ArrayList;
import java.util.List;

public class Logger {
    /*
    В приложении необходимо создать класс Logger, который будет вести журнал событий.
    Класс должен обеспечивать централизованный доступ к логированию для всего приложения.
     Например:
Запись информации о событиях (например, успешное подключение к базе данных).
Запись ошибок (например, исключения при выполнении операций).
Запись предупреждений (например, при попытке работы с устаревшими методами).
Класс Logger должен быть реализован по паттерну Singleton, чтобы в приложении был только один экземпляр этого класса, который будет использоваться для записи логов.

     */
    private static Logger logger;
    private List<String> logEvents;

    private Logger() {
        this.logEvents = new ArrayList<>();
    }

    public static Logger getInstance(){
        if (logger == null){
            logger = new Logger();
        }
        return logger;
    }

    public void addLogEvent(String event) {
        this.logEvents.add(event);
    }

    @Override
    public String toString() {
        return "Logger{" +
                "logEvents=" + logEvents +
                '}';
    }
}
