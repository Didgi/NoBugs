package other_m;

import java.util.*;
import java.util.stream.Collectors;

public class SumInSubArray {
    public static int sumSubArr(int[] arr, int maxBound) {
        final List<Integer> collect = Arrays.stream(arr).boxed().collect(Collectors.toList());
        final List<Integer> integers = collect.subList(0, maxBound);
        return integers.stream().mapToInt(n -> n).sum();
    }

    public static int sumSubArr2(int[] arr, int maxBound) {
        int sum = 0;
        for (int i = 0; i < maxBound; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public static int sumSubArr3(int[] arr, int k) {
        int maxSum = 0;
        int kMax = k;
        int kMin = 0;
        while (true) {
            int sum = 0;
            for (int i = kMin; i < kMax; i++) {
                sum += arr[i];
                if (sum > maxSum) {
                    maxSum = sum;
                }
            }
            if (kMax == arr.length) {
                break;
            }
            kMin++;
            kMax++;
        }
        return maxSum;
    }

    public static int sumSubArr5(int[] arr, int k) {
        int max = 0;
        int previousSum = 0;
        int kFirst = 0;
        int kSecond = k;

        for (int i = kFirst; i < kSecond; i++) {
            previousSum += arr[i];
        }
        max = previousSum;

        for (int i = kSecond; i < arr.length; i++) {
            previousSum = previousSum - arr[kFirst] + arr[kSecond];
            if (max < previousSum) {
                max = previousSum;
            }
            kFirst++;
            kSecond++;
        }
        return max;
    }

    public static void main(String[] args) {
//        System.out.println(sumSubArr3(new int[]{10, 7, 11, 5}, 2));
        System.out.println(findMaxSumInSub(new int[]{1,3,5,2,1}, 2));
//        System.out.println(sumSubArr2(new int[]{1,2,3,4}, 2));
    }

    //int[]{1,3,5,2,1}
    public static int findMaxSumInSub(int[] arr, int k) {
        int maxSum = 0;
        int previousSum = 0;
        int p1 = 0;
        for (int i = 0; i < k; i++) {
            int currentValue = arr[i];
            previousSum += currentValue;
        }
        maxSum = previousSum;
//        p2++;
        for (int i = k; i < arr.length; i++) {
            previousSum = previousSum - arr[p1] + arr[k];
            if (maxSum < previousSum) {
                maxSum = previousSum;
            }
            p1++;
            k++;
        }


//            if (p2) {
//                symbols.remove(str.charAt(p1));
//                p1++;
//            }
//            symbols.add(currentChar);
////            max = Math.max(max, p2 - p1 + 1);
//            if (maxSum < p2 - p1 + 1) {
//                maxSum = p2 - p1 + 1;
//            }
//        }
        return maxSum;
    }


}
