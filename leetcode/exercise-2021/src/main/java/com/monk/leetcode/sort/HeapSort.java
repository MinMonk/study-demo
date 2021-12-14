
package com.monk.leetcode.sort;

public class HeapSort<E extends Comparable> extends Sort<E> {

    private int heapSize;

    @Override
    protected void sort() {
        heapSize = array.length;

        //批量建堆(自下而上的下滤)
        for (int i = (heapSize >> 1) - 1; i >= 0 ; i--) {
            siftDown(i);
        }

        while (heapSize > 1){
            swap(0, --heapSize);

            siftDown(0);
        }
    }


    private void siftDown(int index) {
        E element = array[index];
        int half = heapSize >> 1;
        while (index < half) {
            int left = (index << 1) + 1;
            int right = left + 1;

            int maxIdx = left;
            if (right < heapSize && compare(right, maxIdx) > 0) {
                maxIdx = right;
            }

            // 如果下滤的元素比最大的子节点还要大,那么就退出循环
            if (compare(index, maxIdx) >= 0) break;

            // 如果下滤的元素比最大的子节点小,那么就需要交换位置,并继续下滤
            swap(index, maxIdx);
            index = maxIdx;
        }
        array[index] = element;
    }
}
