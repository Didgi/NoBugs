package practice6_collections;

import java.util.ArrayDeque;

public class ArrayDequeTasks {

    public static void main(String[] args) {

        //firstTask
        ArrayDeque<String> firstExample = new ArrayDeque<>();
        firstExample.add("First");
        firstExample.add("Middle");
        firstExample.addFirst("FirstNew");
        firstExample.addLast("Last");
        firstExample.add("LastNew");
        System.out.println(firstExample);

        //secondTask
        ArrayDeque<String> secondExample = new ArrayDeque<>();
        secondExample.push("First");
        secondExample.push("Second");
        secondExample.push("Third");
        while (!secondExample.isEmpty()){
            System.out.println(secondExample.poll());
        }

        //thirdTask
        System.out.println("Первый элемент в очереди " + firstExample.getFirst());
        System.out.println("Последний элемент в очереди " + firstExample.getLast());

    }
}
