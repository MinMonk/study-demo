package com.monk.test.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2021年7月5日 上午10:01:00
 */
public class ReentrantLockTest {
    
    private final ReentrantLock lock = new ReentrantLock();
    
    private Condition condition = lock.newCondition();
    

}
