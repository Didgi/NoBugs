package cleancode.patterns.structural.facade.second;

public class SmartHomeFacade {
    private Turnable light;
    private Turnable airClimat;
    private Turnable securitySystem;

    public SmartHomeFacade(Turnable light, Turnable airClimat, Turnable securitySystem) {
        this.light = light;
        this.airClimat = airClimat;
        this.securitySystem = securitySystem;
    }

    public void turnOnAllSystems(){
        light.turnOn();
        airClimat.turnOn();
        securitySystem.turnOn();
    }

    public void turnOffAllSystems(){
        light.turnOff();
        airClimat.turnOff();
        securitySystem.turnOff();
    }

}
