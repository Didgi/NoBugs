package cleancode.patterns.structural.facade.first;

public class DoorFacade {
    private OpenDoor openDoor;
    private CloseDoor closeDoor;
    private LockDoor lockDoor;

    public DoorFacade(OpenDoor openDoor, CloseDoor closeDoor, LockDoor lockDoor) {
        this.openDoor = openDoor;
        this.closeDoor = closeDoor;
        this.lockDoor = lockDoor;
    }

    public void openDoor(){
        openDoor.openDoor();
    }

    public void closeDoor(){
        closeDoor.closeDoor();
    }

    public void lockDoor(){
        lockDoor.lockDoor();
    }
}
