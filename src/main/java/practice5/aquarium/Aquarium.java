package practice5.aquarium;

public class Aquarium {
    private SeaEntity seaEntity;

    public void setSeaEntity(SeaEntity seaEntity){
        this.seaEntity = seaEntity;
    }

    public void showBehavior(){
        this.seaEntity.move();
    }

}
