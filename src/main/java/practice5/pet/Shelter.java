package practice5.pet;

public class Shelter {

    private Pet pet;

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public void interact(){
        this.pet.feed();
        this.pet.interact();
    }
}
