package com.monk.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @ClassName RedisLock
 * @Description: TODO
 * @Author Monk
 * @Date 2021/8/4
 * @Version V1.0
 **/
public class RedisLock implements Lock {

    private static Jedis jedis;

    private ThreadLocal<String> local = new ThreadLocal<String>();
    static{
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxWaitMillis(3000);

        JedisPool pool = new JedisPool(jedisPoolConfig,"192.168.94.200", 6379, 3000);
        jedis = pool.getResource();
        if(null == jedis){
            throw new RuntimeException("锁初始化异常");
        }
    }

    /**
     * 初始化连接池
     *
     * Lock   -->
     *      1. 往redis中写入一个10s过期key=redis，value=uuid的值
     *          setex lock UUID.randomUUid() 10
 *          2. 往redis中写入成功后，往当前虚拟机中的ThreadLocal中也写入一份
 *          3. tryLock失败，就线程休眠一会儿，待会儿再试，外面是一个死循环
     *
     * tryLock  -->   如果setnx失败，就说明锁还存在，就返回锁失败
     *
     * unLock  -->    get lock 得到的值于TheadLocal中的值进行比较，不一样则抛出异常
     *      一样则删除这个key   del lock
     *
     */

    @Override
    public void lock() {
        for(;;)
        {
            if(tryLock()){
                String uuid = UUID.randomUUID().toString();
                jedis.setex("lock", 10L, uuid);
                local.set(uuid);
                break;
            }else{
                try {
                    TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
