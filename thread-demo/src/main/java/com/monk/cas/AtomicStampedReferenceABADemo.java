package com.monk.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * CAS:AtomicStampedReference</br>
 * 利用AtomicStampedReference来解决CAS下的ABA问题
 * AtomicStampedReference在AtomicInteger的基础上增加了stamp，通过stamp来解决ABA问题
 * stamp相当于一个版本号
 */
public class AtomicStampedReferenceABADemo {

    public static void main(String[] args) throws InterruptedException {
        AtomicStampedReference<Integer> stampInt = new AtomicStampedReference<>(1, 1);
        new Thread(() -> {
            System.out.println("第1次版本号：" + stampInt.getStamp());
            stampInt.compareAndSet(1, 2, stampInt.getStamp(), stampInt.getStamp() + 1);
            System.out.println("第2次版本号：" + stampInt.getStamp());
            stampInt.compareAndSet(2, 1, stampInt.getStamp(), stampInt.getStamp() + 1);
            System.out.println("第3次版本号：" + stampInt.getStamp());
        }).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(stampInt.getReference() + ", stamp:" + stampInt.getStamp());


        new Thread(() -> {
            System.out.println("第1次版本号：" + stampInt.getStamp());
            boolean flag = stampInt.compareAndSet(1, 10, 1, stampInt.getStamp() + 1);
            System.out.println("修改结果：" + flag);
        }).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(stampInt.getReference() + ", stamp:" + stampInt.getStamp());
    }
}
