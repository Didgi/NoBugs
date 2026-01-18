package cleancode.patterns.structural.facade.first_2;

public class Main {
    public static void main(String[] args) {

    DoorFacade doorFacade = new DoorFacade();
    doorFacade.open();
    doorFacade.close();
    doorFacade.lock();
    }

}
