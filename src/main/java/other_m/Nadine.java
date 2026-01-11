package other_m;

import java.util.Arrays;

public class Nadine {
    /**
     * Есть массив не отсортированных чисел.
     * Задача: найти наименьшее положительное отсутствутющее число в массиве
     * int[] ints = {1, 2}; -> 3
     * int[] ints = {3, 1, 4}; -> 2
     * int[] ints = {2, 1, 4}; -> 3
     * int[] ints = {1}; -> 2
     */
    public static void main(String[] args) {
        System.out.println(minAbsNumber3(new int[]{1,2}));
        System.out.println(minAbsNumber3(new int[]{2,1,4}));
        System.out.println(minAbsNumber3(new int[]{3,1,4}));
        System.out.println(minAbsNumber3(new int[]{1}));
    }

    public static int minAbsNumber(int[] arr) {
        final int[] array = Arrays.stream(arr).sorted().toArray();
        if (array.length == 1) {
            if (array[array.length - 1] == 1) {
                return array[array.length - 1] + 1;
            } else {
                return array[array.length - 1] - 1;

            }
        } else {
            for (int i = 0; i < array.length-1; i++) {
                if (!(array[i+1] - array[i] == 1)) {
                    return array[i]+1;
                }
            }
        }
    return array[array.length-1] + 1;
    }

    public static int minAbsNumber3(int[] arr) {
        int[] finalArr = new int[20];
        for (int i = 0; i < arr.length; i++) {
            finalArr[arr[i]-1] = 1;
        }
        for (int i = 0; i < finalArr.length; i++) {
            if (finalArr[i] == 0){
                return i + 1;
            }
        }
        return -1;
    }
}
