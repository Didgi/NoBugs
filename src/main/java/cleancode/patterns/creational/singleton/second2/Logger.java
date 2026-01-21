package cleancode.patterns.creational.singleton.second2;

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
    private List<String> logEvents;

    private Logger() {
        this.logEvents = new ArrayList<>();
    }

    public static class Helper{
        static final Logger instance = new Logger();
    }

    public static Logger getInstance(){
        return Helper.instance;
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
