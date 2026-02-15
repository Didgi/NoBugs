package practice5.farm;

public class Cow implements CareableFunctionable {

    @Override
    public void care() {
        System.out.println("Коровка нуждается в выпасе");
    }

    @Override
    public void function() {
        System.out.println("Корова даёт молоко");
    }
}
