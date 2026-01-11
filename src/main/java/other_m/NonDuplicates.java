package other_m;

import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NonDuplicates {
    public static void main(String[] args) {
        int[] nums = new int[]{4, 3, 2, 4, 1, 3, 2};
        int[] nums2 = new int[]{4, 3, 2, 1, 3, 2, 1};
        int[] nums3 = new int[]{4, 4};
//
        System.out.println(checkDuplicates2(nums));
//
//        task1();

    }

    public static int checkDuplicates(int[] nums) {
        List<Integer> listNum = Arrays.stream(nums).boxed().collect(Collectors.toList());
        List<Integer> duplicatesNum = new ArrayList<>();
        int uniq = 0;
        make:
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (duplicatesNum.contains(nums[i])) {
                    uniq = -1;
                    break;
                }
//                if (duplicatesNum.isEmpty()) {
//                    uniq = nums[i];
//                }
                if (nums[i] == nums[j]) {
                    duplicatesNum.add(nums[i]);
                    uniq = -1;
                    break;
                }
            }
            {
                uniq = nums[i];
                break make;
            }
//            uniq = nums[i];
        }
        return uniq;
    }

    public static int checkDuplicates2(int[] nums) {
        final List<Integer> collect = Arrays.stream(nums).boxed().collect(Collectors.toList());
        Map<Integer, Integer> mapNums = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            mapNums.put(nums[i], mapNums.getOrDefault(nums[i], 0) + 1);
        }
        int result = -1;
        for (Map.Entry<Integer, Integer> entries: mapNums.entrySet()) {
            if (entries.getValue() == 1){
                result = entries.getKey();
                break;
            }
        }

        final Integer i = mapNums.entrySet().stream().filter(entry -> entry.getValue() == 1).map(Map.Entry::getKey).findFirst().orElse(-1);
        return i;
//        return result;
    }

    public static void task1() {
        Stream.of("d2", "a2", "b1", "b3", "c")
                .map(s -> {
                    System.out.println("map: " + s);
                    return s.toUpperCase();
                })
                .anyMatch(s -> {
                    System.out.println("anyMatch: " + s);
                    return s.startsWith("A");
                });
    }
}
