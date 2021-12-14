package com.monk.leetcode.structure.heap;


import com.monk.leetcode.structure.tree.printer.BinaryTrees;

import java.util.Comparator;

public class HeapTest {

    public static void main(String[] args) {
        // testBinaryHeap();

        test3();
    }

    /**
     * 测试二叉大顶 堆
     */
    private static void testBinaryHeap() {
        BinaryHeap<Integer> heap = new BinaryHeap<>();
        heap.add(13);
        heap.add(6);
        heap.add(23);
        heap.add(56);
        heap.add(34);
        heap.add(78);
        heap.add(12);
        heap.add(29);
        BinaryTrees.println(heap);
        heap.remove();
        BinaryTrees.println(heap);
        heap.replace(8);
        BinaryTrees.println(heap);
    }

    static void test2(){
        Integer arrs[] = new Integer[]{
                13,15,24,89,10,56,87,189,133,120,119
        };

        // 大顶堆
        BinaryHeap<Integer> maxHeap = new BinaryHeap<>(arrs);
        BinaryTrees.println(maxHeap);

        // 小顶堆
        BinaryHeap<Integer> minHeap = new BinaryHeap<>(arrs, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        BinaryTrees.println(minHeap);
    }

    /**
     * 测试TopN问题
     */
    static void test3(){
        Integer arrs[] = new Integer[]{
                13,15,24,89,10,56,87,189,133,120,119
        };

        BinaryHeap<Integer> minHeap = new BinaryHeap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });

        int topN = 3;
        for (int i = 0; i < arrs.length; i++) {
            if(i < topN){
                minHeap.add(i);
            }else if(arrs[i] > minHeap.get()){
                minHeap.replace(arrs[i]);
            }
        }

        BinaryTrees.println(minHeap);
    }
}
