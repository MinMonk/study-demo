package com.monk.rule.loop;

public class RequestSequence {

    public static Integer sequenceNum = 0;

    public synchronized static Integer getAndIncreatement(){
        return sequenceNum++;
    }
}
