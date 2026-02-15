package practice7_exception_generics.additionalTasks;

import java.util.Map;

public class MapsClass<K, V> {

    public void getMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println(entry);
        }
    }

}
