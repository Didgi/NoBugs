package testLesson;

import java.util.Arrays;

public class TestLesson {
    /*
    Найти макс сумму подмассива длиной К
    Шаг 1: примеры.
    k = 2. {1,2,3,4} -
    Шаг 2: алгоритм
    Запоминаем сумму и максимальное значение
    Суммируем все элементы массива до тех пор пока не дойдём до К элемента
    Из общей сумму вычитаем первый элемент массива
    В общую сумму прибавляем к+1 элемент массива

     */


    public static int findSubMax(int[] nums, int k) {
        int sum = 0;
        int max = 0;
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (i >= k) {
                sum -= nums[i - k];
//                sum += nums[i + k - 1];
            }

            if (i >= k - 1) {
                max = Math.max(sum, max);}

        }
        return max;
    }

    public static int findSubMaxArr(int[] nums, int k) {
        int sum = 0;
        int max = 0;
        int j = 0;
        for (j = 0; j <= k; j++) {
            sum+= nums[j];
            max = sum;
        }
        while (j < nums.length){
            sum-=nums[j];
            sum+=nums[j++];
        }

        return max;
    }

    public static int findBiggestSubarray(int[] array, int k){
        if (k < 1) {
            throw new IllegalArgumentException("Subarray cant be lower than 1");
        }

        if (k == 1) {
            return Arrays.stream(array).max().getAsInt();
        }

        if (k >= array.length){
            return Arrays.stream(array).sum();
        }

        int sum = 0;
        int maxSum = 0;

        //sum of first K elements
        for (int i = 0; i < k; i++){
            sum += array[i];
            maxSum = sum;
        }

        for (int i = k; i < array.length; i++){
            sum += array[i];
            sum -= array[i-k];
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        int[] testArr = {1,3,2,5,2};
        System.out.println(findBiggestSubarray(testArr, 2));
        System.out.println(findSubMax(testArr, 2));
    }
}
