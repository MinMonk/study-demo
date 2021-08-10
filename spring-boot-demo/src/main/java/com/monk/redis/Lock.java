package com.monk.redis;

public interface Lock {
    
    void lock(String key);
    
    boolean tryLock(String key);
    
    void unlock(String key);

}
