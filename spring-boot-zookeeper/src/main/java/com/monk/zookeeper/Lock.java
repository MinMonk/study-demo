package com.monk.zookeeper;

public interface Lock {
    
    void lock(String key);
    
    boolean tryLock(String key);
    
    void unlock(String key);

}
