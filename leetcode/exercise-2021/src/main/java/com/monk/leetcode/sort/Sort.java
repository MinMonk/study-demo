package com.monk.leetcode.sort;

import java.text.DecimalFormat;

public abstract class Sort<E extends Comparable> implements Comparable<Sort<E>> {

    protected E[] array;

    protected int cmpCount;
    protected int swapCount;
    private long time;

    private DecimalFormat fmt = new DecimalFormat("#0.00");

    public void sort(E[] array) {
        if (null == array || array.length <= 0) return;
        this.array = array;

        //printArr("排序前:", array);
        long start = System.currentTimeMillis();
        sort();
        time = System.currentTimeMillis() - start;
        // printArr("排序后: ", array);
    }

    protected abstract void sort();

    /**
     * 比较两个元素的大小
     * > 0  说明idx1 > idx2
     * = 0  说明idx1 = idx2
     * < 0  说明idx1 < idx2
     *
     * @param idx1
     * @param idx2
     * @return
     */
    protected int compare(int idx1, int idx2) {
        cmpCount++;
        return array[idx1].compareTo(array[idx2]);
    }

    protected int compare(E e1, E e2) {
        cmpCount++;
        return e1.compareTo(e2);
    }

    /**
     * 比较两个元素的大小
     *
     * @param e1
     * @param e2
     * @return
     */
    /*protected int compare(Integer e1, Integer e2) {
        cmpCount++;
        return e1 - e2;
    }*/

    /**
     * 交换两个元素的值
     *
     * @param idx1
     * @param idx2
     */
    protected void swap(int idx1, int idx2) {
        swapCount++;
        E tmp = array[idx2];
        array[idx2] = array[idx1];
        array[idx1] = tmp;
    }

    private String numberString(int number) {
        if (number < 10000) return "" + number;
        if (number < 100000000) return fmt.format(number / 10000) + "万";
        return fmt.format(number / 100000000) + "亿";
    }

    /**
     * 打印数组
     *
     * @param arr 数组
     */
    public void printArr(String str, Integer[] arr) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        for (int i = 0; i < arr.length; i++) {
            String split = ", ";
            if (i == arr.length - 1) {
                split = "";
            }
            sb.append(arr[i] + split);
        }
        String timeStr = "耗时:" + (time / 1000) + "(" + time + "ms)";
        String cmpStr = "比较" + numberString(cmpCount) + "次";
        String swapStr = "交换" + numberString(swapCount) + "次";
        sb.append("\t").append(timeStr).append("\t").append(cmpStr).append("\t").append(swapStr);
        sb.append("\t sort by [").append(getClass().getSimpleName()).append("]");

        System.out.println(sb.toString());
    }

    @Override
    public int compareTo(Sort o) {
        int result = (int) (this.time - o.time);
        if (result != 0) return result;
        result = this.cmpCount - o.cmpCount;
        if (result != 0) return result;
        result = this.swapCount - o.swapCount;
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("排序后:");
        for (int i = 0; i < array.length; i++) {
            String split = ", ";
            if (i == array.length - 1) {
                split = "";
            }
            sb.append(array[i] + split);
        }
        String timeStr = "耗时:" + (time / 1000) + "(" + time + "ms)";
        String cmpStr = "比较" + numberString(cmpCount) + "次";
        String swapStr = "交换" + numberString(swapCount) + "次";
        String stableStr = "稳定性:" + isStable();
        sb.append("\t").append(timeStr).append("\t").append(cmpStr).append("\t").append(swapStr);
        sb.append("\t").append(stableStr);
        sb.append("\t sort by [").append(getClass().getSimpleName()).append("]");
        return sb.toString();
    }

    private boolean isStable() {

        // 希尔排序比较特殊,他是一个不稳定的排序算法
        if(this instanceof ShellSort) return false;

        Student[] students = new Student[20];
        for (int i = 0; i < students.length; i++) {
            students[i] = new Student(10, i * 10);
        }

        sort((E[]) students);
        for (int i = 0; i < students.length - 1; i++) {
            int score = students[i].score;
            int score2 = students[i + 1].score;
            if (score2 != score + 10) return false;
        }
        return true;

    }

    private static class Student implements Comparable<Student> {
        private int age;
        private int score;

        public Student(int age, int score) {
            this.age = age;
            this.score = score;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        @Override
        public int compareTo(Student o) {
            return this.age - o.age;
        }
    }
}
