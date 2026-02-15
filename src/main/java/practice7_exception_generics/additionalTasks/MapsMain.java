package practice7_exception_generics.additionalTasks;

import java.util.HashMap;

public class MapsMain {
    public static void main(String[] args) {

        MapsClass<Number, String> mapsClass = new MapsClass<>();
        HashMap<Number, String> hashMap = new HashMap<>();
        hashMap.put(1, "first");
        hashMap.put(2, "second");
        mapsClass.getMap(hashMap);
    }
}
