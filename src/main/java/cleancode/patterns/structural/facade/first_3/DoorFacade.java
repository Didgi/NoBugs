package cleancode.patterns.structural.facade.first_3;

public class DoorFacade {
    private Actionable openDoor;
    private Actionable closeDoor;
    private Actionable lockDoor;

    public DoorFacade(Actionable openDoor, Actionable closeDoor, Actionable lockDoor) {
        this.openDoor = openDoor;
        this.closeDoor = closeDoor;
        this.lockDoor = lockDoor;
    }

    public void openDoor(){
        openDoor.action();
    }

    public void closeDoor(){
        closeDoor.action();
    }

    public void lockDoor(){
        lockDoor.action();
    }
}
