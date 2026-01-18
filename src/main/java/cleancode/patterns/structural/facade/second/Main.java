package cleancode.patterns.structural.facade.second;

public class Main {
    /*
    В системе "Умный дом" есть несколько устройств, которые управляют:
Светом (включить/выключить).
Кондиционером (включить/выключить).
Системой безопасности (включить/выключить).
Клиенту нужно предоставить единый интерфейс для управления (включение/выключение) всеми этими устройствами,
 скрыв детали реализации каждого из них.
 Используем фасад, который объединит все эти операции в один интерфейс.
     */

    public static void main(String[] args) {
        Light light = new Light();
        AirClimat airClimat = new AirClimat();
        SecuritySystem securitySystem = new SecuritySystem();
        SmartHomeFacade smartHomeFacade = new SmartHomeFacade(light, airClimat, securitySystem);
        smartHomeFacade.turnOnAllSystems();
        smartHomeFacade.turnOffAllSystems();
    }
}
