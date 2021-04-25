package com.monk.rule.random;

import com.monk.rule.ServerMachines;

import java.util.Random;

/**
 * 随机算法-版本1.0
 */
public class RadomRobinV1 {

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){
            System.out.println(getServer());
        }
    }

    public static String getServer(){
        Random random = new Random();
        int idx = random.nextInt(ServerMachines.SERVER_LIST.size());
        return ServerMachines.SERVER_LIST.get(idx);
    }
}
