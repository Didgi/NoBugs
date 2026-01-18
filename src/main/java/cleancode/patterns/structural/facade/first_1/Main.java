package cleancode.patterns.structural.facade.first_1;

public class Main {
    public static void main(String[] args) {

    OpenDoor openDoor = new OpenDoor();
    CloseDoor closeDoor = new CloseDoor();
    LockDoor lockDoor = new LockDoor();

    DoorFacade doorFacade = new DoorFacade(openDoor, closeDoor, lockDoor);
    doorFacade.manageDoor();
    }

}
