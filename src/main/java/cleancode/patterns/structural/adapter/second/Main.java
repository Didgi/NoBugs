package cleancode.patterns.structural.adapter.second;

public class Main {
    /*
    В проекте есть система, которая работает с мильными единицами (например, старая система),
    но новая система должна использовать километры. Нужно создать адаптер,
    который будет преобразовывать мили в километры, чтобы новая система могла работать с ними,
    не изменяя её логику.
     */
    public static void main(String[] args) {
        Miles miles = new Miles(100);
        MilesToKmAdapter milesToKmAdapter = new MilesToKmAdapter(miles);
        System.out.println(milesToKmAdapter.convertMilesToKm());
    }
}
