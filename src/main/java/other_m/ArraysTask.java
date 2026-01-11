package other_m;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArraysTask {
    /**
     * Сложить два отсортированных массив в один общий отсортированный по возрастанию
     * <p>
     * [1,3,5]
     * [2,4,7]
     * ОР: [1,2,3,4,5,7]
     * <p>
     * Доп вопросы:
     * <p>
     * Алгоритм:
     * Сложить два массива в один
     * Отсортировать по возрастанию
     * Вернуть массив
     */


    public static void main(String[] args) {
        //Слияние массивов
//        System.out.println(java.util.Arrays.toString(concArrays1(new int[]{1, 3, 5}, new int[]{2, 4, 7})));
//        System.out.println(java.util.Arrays.toString(concArrays(new int[]{1, 1, 5}, new int[]{2, 4, 7})));
//        System.out.println(maxIntInArray(new int[]{5, 3, 4, 1}));
        //

//        System.out.println(java.util.Arrays.toString(removeDuplicates3(new int[]{2, 2, 1, 3, 5, 3, 6})));
//        System.out.println(isContainDuplicates(new int[]{2, 2, 1, 3, 5, 3, 6}));
//        System.out.println(isContainDuplicates(new int[]{2, 1, 5, 3, 6}));
//        System.out.println(Arrays.toString(reverseArray(new int[]{1, 10, 2, 3, 4, 5})));
//        System.out.println(Arrays.toString(reverseArrayAdvanced(new int[]{1, 10, 2, 3, 4, 5})));
        System.out.println(Arrays.toString(removeNum(new int[]{1, 10, 2, 3, 4, 5}, 2)));
        System.out.println(Arrays.toString(removeNum2(new int[]{1, 10, 2, 3, 4, 5}, 2)));
        //[2,2,1,3,5,3,6] -> [2,1,3,5,6]

    }

    public static int[] concArrays1(int[] arr1, int[] arr2) {
        LinkedList<Integer> ll = new LinkedList<>();
        TreeSet<Integer> listOrder = new TreeSet<>();
        for (int i : arr1) {
            listOrder.add(i);
        }
        for (int i : arr2) {
            listOrder.add(i);
        }
        return listOrder.stream().mapToInt(n -> n).toArray();
//     return IntStream.concat(java.util.Arrays.stream(arr1), java.util.Arrays.stream(arr2)).sorted().toArray();
    }

    public static int[] concArrays(int[] arr1, int[] arr2) {
        final List<Integer> collect = java.util.Arrays.stream(arr1).boxed().collect(Collectors.toList());
        final List<Integer> collect2 = java.util.Arrays.stream(arr2).boxed().collect(Collectors.toList());
        TreeSet<Integer> newArr = new TreeSet<>(collect);
        newArr.addAll(java.util.Arrays.stream(arr2).boxed().collect(Collectors.toList()));
        return newArr.stream().mapToInt(n -> n.intValue()).toArray();
    }

    //найти второе максимальное число в массиве
    public static int maxIntInArray(int[] arr) {

        return java.util.Arrays.stream(arr).boxed().sorted(Comparator.reverseOrder())
                .skip(1).mapToInt(n -> n).findFirst().orElse(0);

    }

    //удалить дубликаты из массива сохранив порядок
//    [2,2,1,3,5,3,6] -> [2,1,3,5,6]

    public static int[] removeDuplicates(int[] arr) {
        List<Integer> uniqNums = new LinkedList<>();
        for (int i : arr) {
            if (!uniqNums.contains(i)) {
                uniqNums.add(i);
            }
        }

        return uniqNums.stream().mapToInt(n -> n).toArray();
    }

    public static int[] removeDuplicates2(int[] arr) {
        Set<Integer> uniqNums = new LinkedHashSet<>();
//        uniqNums.add(java.util.Arrays.stream(arr).boxed().toArray());
        for (int i : arr) {
//            if (!uniqNums.contains(i)) {
            uniqNums.add(i);
//            }
        }

        return uniqNums.stream().mapToInt(n -> n).toArray();
    }

    public static int[] removeDuplicates3(int[] arr) {
        return java.util.Arrays.stream(arr).distinct().toArray();
    }

    public static boolean isContainDuplicates(int[] arr) {
        Set<Integer> uniqNums = new HashSet<>();
        for (int i : arr) {
            if (!uniqNums.add(i)) return true;
        }

        return false;
    }

    //[1,2,3,4 ] -> [4, 3, ]

    public static int[] reverseArray(int[] arr) {
        int[] incomeArray = arr;
        for (int i = 0; i < incomeArray.length / 2; i++) {
            int arrSize = incomeArray.length - i - 1;
            int temp = arr[i];
            incomeArray[i] = incomeArray[arrSize];
            incomeArray[arrSize] = temp;
        }
        return incomeArray;
    }

    public static int[] reverseArrayAdvanced(int[] arr) {
        return IntStream.range(0, arr.length).map(i -> arr[arr.length - i - 1]).toArray();
    }

    //Удалить все вхождения определённого числа
    //int[1,2,3,4,5], 2 -> 1,3,4,5

    public static int[] removeNum(int[] arr, int num){
       return Arrays.stream(arr).filter(n -> n != num).toArray();
    }

    public static int[] removeNum2(int[] arr, int num){
        List<Integer> listNums = Arrays.stream(arr).boxed().collect(Collectors.toList());

        for (int i = 0; i < listNums.size(); i++) {
            if (listNums.get(i) == num){
                listNums.remove(i);
            }
        }

        return listNums.stream().mapToInt(n->n).toArray();
    }
}
