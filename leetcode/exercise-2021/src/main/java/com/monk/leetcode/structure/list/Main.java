package com.monk.leetcode.structure.list;

public class Main {
    public static void main(String[] args) {
        CustomList<Integer> list = new DoubleCircleLinkedList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(11);
        list.add(4);
        list.add(5);

        list.set(3, 3);

        list.remove(0);//1,2,3,4,5
        // list.remove(4);//1,2,3,4
        // list.remove(1);//1,3,4
        // list.remove(5);//1,3,4

        System.out.println(list);
        josephus(3);  //3,6,1,5,2,8,4,7
    }

    /**
     * 约瑟夫环
     * @param n 跳过的数
     */
    static void josephus(int n) {
        DoubleCircleLinkedList<Integer> list = new DoubleCircleLinkedList<>();
        for (int i = 1; i <= 8; i++) {
            list.add(i);
        }

        // 指向头结点（指向1）
        list.reset();

        while (!list.isEmpty()) {
            for (int i = n; i > 1; i--) {
                list.next();
            }
            System.out.print(list.remove() + ",");
        }
    }
}
