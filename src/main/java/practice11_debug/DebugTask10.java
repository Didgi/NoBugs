package practice11_debug;

import java.util.*;
import java.util.stream.Collectors;

public class DebugTask10 {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(Arrays.asList("Alice", "Bob", "Charlie"));
        final Iterator<String> iterator = names.iterator();
        if (iterator.hasNext()) {
            if (iterator.next().startsWith("A")) {
                iterator.remove();
            }
        }
        System.out.println("Updated list: " + names);
    }
}
