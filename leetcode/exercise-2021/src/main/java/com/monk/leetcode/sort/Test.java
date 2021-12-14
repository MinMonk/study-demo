package com.monk.leetcode.sort;


import com.monk.leetcode.tools.ArrayUtils;

import java.util.Arrays;

public class Test {

    public static void main(String args[]) {
        Integer[] array = ArrayUtils.buildRandomArray2(10);
        ArrayUtils.printArr2("排序前: ", array);

        testSort(array, new Sort[]{
                new BubbleSort(),
                new BubbleSort1(),
                new BubbleSort2(),
                new SelectionSort(),
                new HeapSort(),
                new InsertionSort(),
                new InsertionSort1(),
                new InsertionSort2(),
                new MergeSort(),
                new QuickSort(),
                new ShellSort()
        });
    }

    static void testSort(Integer[] array, Sort... sorts){
        for(Sort sort : sorts){
            sort.sort(ArrayUtils.copy(array));
        }

        Arrays.sort(sorts);

        for(Sort sort : sorts){
            System.out.println(sort);
        }

    }
}
