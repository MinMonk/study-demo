package com.monk.test.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * 信号量测试类
 * @author Monk
 * @version V1.0
 * @date 2021年7月8日 下午4:00:13
 */
@Slf4j
public class SemaphoreTest {

    @Test
    public void testSemaphore() {
        Semaphore semaphore = new Semaphore(3, true);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    log.info("{}获得了信号量", Thread.currentThread().getName());
                    TimeUnit.SECONDS.sleep(2L);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }, "thread-" + i).start();
        }
        
        while(true);
    }

}
