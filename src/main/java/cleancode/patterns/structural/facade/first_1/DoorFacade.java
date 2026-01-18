package cleancode.patterns.structural.facade.first_1;

public class DoorFacade {
    private OpenDoor open;
    private CloseDoor close;
    private LockDoor lock;

    public DoorFacade(OpenDoor open, CloseDoor close, LockDoor lock) {
        this.open = open;
        this.close = close;
        this.lock = lock;
    }

    public void manageDoor(){
        open.openDoor();
        close.closeDoor();
        lock.lockDoor();
    }
}
