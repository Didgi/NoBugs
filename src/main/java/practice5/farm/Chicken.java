package practice5.farm;

public class Chicken implements CareableFunctionable{
    @Override
    public void care() {
        System.out.println("Курица требует корм с зерном");
    }

    @Override
    public void function() {
        System.out.println("Курица несёт яйца");
    }
}
