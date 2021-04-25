package com.monk.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS下的ABA问题
 * <p>
 * 一个线程把数据A变为了B，然后又重新变成了A，此时另外一个线程读取的时候，
 * 发现A没有变化，就误以为是原来的那个A。这就是有名的ABA问题
 *
 * <p>
 * 线程1将变量1  >>   2   >>  1
 * 线程2再次修改时，误以为当前的1还是之前的1，从而修改成功</br>
 * <p>
 * 举例：
 * 一个小偷，把别人家的钱偷了之后又还了回来，还是原来的钱吗，你老婆出轨之后
 * 又回来，还是原来的老婆吗？ABA问题也一样，如果不好好解决就会带来大量的问题。
 * 最常见的就是资金问题，也就是别人如果挪用了你的钱，在你发现之前又还了回来，
 * 但是别人却已经触犯了法律。
 */
public class AtomicIntegerABADemo {

    private static AtomicInteger atomicInteger = new AtomicInteger(1);

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            atomicInteger.compareAndSet(1, 2);
            atomicInteger.compareAndSet(2, 1);
        }).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(atomicInteger.get());

        new Thread(() -> {
            atomicInteger.compareAndSet(1, 10);
        }).start();
        TimeUnit.SECONDS.sleep(1);
        System.out.println(atomicInteger.get());
    }
}
