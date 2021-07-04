package com.monk.utils.queue;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
class ReadThead implements Runnable{

    ReadThead(ArrayBlockingQueue<String> queue){
        this.queue = queue;
    }

    private ArrayBlockingQueue<String> queue;

    @Override
    public void run() {
        try {
            log.info("读线程先睡眠1s钟,让写线程往队列中写入一些消息");
            Thread.sleep(1000);
            log.info("读线程开始读消息了");
            while (queue.iterator().hasNext()){
                log.info("{}", queue.take());
            }

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}

@Slf4j
class WriteThead implements Runnable{

    WriteThead(ArrayBlockingQueue<String> queue){
        this.queue = queue;
    }

    private ArrayBlockingQueue<String> queue;

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                queue.put("msg_" + i);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}

@Slf4j
public class ArrayBlockingQueueTest {

    @Test
    public void test() throws Exception {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10);

        Thread write = new Thread(new WriteThead(queue));
        write.setName("writeThead");
        write.start();

        Thread read = new Thread(new ReadThead(queue));
        read.setName("readThread");
        read.setDaemon(true);
        read.start();
    }
}
