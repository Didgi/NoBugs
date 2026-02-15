package practice7_exception_generics.additionalTasks;

import java.util.ArrayList;

public class NumberBoxMain {


    public static void main(String[] args) {

        //task for NumberBox
        NumberBox<Integer> numberBoxInt = new NumberBox<>();
        System.out.println(numberBoxInt.sumNums(1,5));
        NumberBox<Long> numberBoxLong = new NumberBox<>();
        System.out.println(numberBoxLong.sumNums(100000000L,55555L));
        NumberBox<Number> numberBoxNumber = new NumberBox<>();
        System.out.println(numberBoxNumber.sumNums(10000d,5d));

        //task for List<T extends Number>
        numberBoxInt.sumNums2(1L,5L);

        //task for List<T super Integer>
        numberBoxInt.addNumToList(new ArrayList<>());


    }
}
