package other_m;

import java.util.Arrays;

public class MaxMinValueInArray {
    public static void main(String[] args) {
        int[] nums = new int[]{1,5,3, 0};
        System.out.println(findMaxValue2(nums));
//        findMinValue(nums);

    }

    public static void findMaxValue(int[] nums){
        int max = nums[0];

        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }
        System.out.println(max);
    }

    public static int findMaxValue2(int[] nums){
        return Arrays.stream(nums).max().orElse(0);
    }

    public static void findMinValue(int[] nums){
        int min = nums[0];

        for (int num : nums) {
            if (num < min) {
                min = num;
            }
        }
        System.out.println(min);
    }
}
