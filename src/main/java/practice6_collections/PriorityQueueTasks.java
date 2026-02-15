package practice6_collections;

import java.util.Arrays;
import java.util.PriorityQueue;

public class PriorityQueueTasks {

    public static void main(String[] args) {

        PriorityQueue<Integer> example = new PriorityQueue<>(Arrays.asList(3, 10, 1, 5, 2));
        while (!example.isEmpty()) {
            System.out.println(example.remove());
        }

    }
}
