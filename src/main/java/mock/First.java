package mock;

import java.util.Arrays;

public class First {
    /**
     * Найти в массиве второй минимум
     *
     * int[]{3,1,2} -> 2
     * int[]{3,1,2,1} -> 2
     * int[]{1} -> exception
     * int[]{} -> exception
     *
     * Необходимо отсортировать массив по возрастанию
     * Затем взять 1 индекс из нового массива
     */

    public static int getSecondMinValue(int[] arr){
//        if (arr.length <=1) {
        if (arr.length ==0) {
            throw new IllegalArgumentException("Массив содержит 1 элемент или массив пустой");
        }
        return Arrays.stream(arr).sorted().skip(1).findFirst().orElse(0);
    }

    public static void main(String[] args) {
//        System.out.println(getSecondMinValue(new int[]{3, 1, 2}));
        System.out.println(getSecondMinValue(new int[]{3,1,2,1}));
//        System.out.println(getSecondMinValue(new int[]{1}));
//        System.out.println(getSecondMinValue(new int[]{}));
    }
}
