package practice5.farm;

public class Farm {

    public void manage(CareableFunctionable animalCareAndFunction){
        animalCareAndFunction.care();
        animalCareAndFunction.function();
    }
}
