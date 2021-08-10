package com.monk.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @ClassName RedisLock
 * @Description: TODO
 * @Author Monk
 * @Date 2021/8/4
 * @Version V1.0
 **/
public class RedisLock implements Lock {

    private static JedisPool pool;

    private static final String LOCK_KEY_PREFIX = "lock_";

    private AtomicBoolean dogRunning = new AtomicBoolean(false);

    private ThreadLocal<String> local = new ThreadLocal<String>();
    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(100);
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxWaitMillis(3000);

        pool = new JedisPool(jedisPoolConfig, "192.168.94.200", 6379, 3000);
    }

    /**
     * 初始化连接池
     *
     * Lock --> 1. 往redis中写入一个10s过期key=redis，value=uuid的值 setex lock UUID.randomUUid() 10 2.
     * 往redis中写入成功后，往当前虚拟机中的ThreadLocal中也写入一份 3. tryLock失败，就线程休眠一会儿，待会儿再试，外面是一个死循环
     *
     * tryLock --> 如果setnx失败，就说明锁还存在，就返回锁失败
     *
     * unLock --> get lock 得到的值于TheadLocal中的值进行比较，不一样则抛出异常 一样则删除这个key del lock
     *
     */

    @Override
    public void lock(String key) {
        for (;;) {
            if (tryLock(key)) {
                break;
            } else {
                try {
                    TimeUnit.MILLISECONDS.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean tryLock(String key) {
        Jedis jedis = pool.getResource();
        String uuid = UUID.randomUUID().toString();
        SetParams cmd = new SetParams();
        cmd.ex(2l);
        cmd.nx();
        String lock = jedis.set(LOCK_KEY_PREFIX + key, uuid, cmd);
        jedis.close();
        if ("OK".equalsIgnoreCase(lock)) {
            local.set(uuid);
            if (!dogRunning.get()) {
                Thread dog = new Thread(() -> {
                    Jedis con = pool.getResource();
                    while (true) {
                        Long ttl = con.ttl(LOCK_KEY_PREFIX + key);
                        if (null != ttl && ttl > 0) {
                            con.expire(LOCK_KEY_PREFIX + key, (ttl + 1));
                        }
                        try {
                            TimeUnit.SECONDS.sleep(1L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dog.setDaemon(true);
                dog.start();
                dogRunning.set(true);
            }
            return true;
        }

        return false;
    }

    @Override
    public void unlock(String key) {
        String script = "if redis.call(\"get\", KEYS[1]) == ARGV[1] then \n"
                + "return redis.call(\"del\", KEYS[1]) \n" + "else \n" + "return 0 \n" + "end";
        Jedis jedis = pool.getResource();
        try {
            Object eval = jedis.eval(script, Arrays.asList(LOCK_KEY_PREFIX + key), Arrays.asList(local.get()));
            if (Integer.valueOf(eval.toString()) == 0) {
                throw new RuntimeException("解锁失败");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
    }

}
