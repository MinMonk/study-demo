package com.monk.rule.random;

import com.monk.rule.ServerMachines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机算法-版本v2
 * 增加权重选项
 * 将服务器IP地址按照权重copy到一个新的List<String>中,再产生随机数,从这个新的List中get对应的IP
 */
public class WeightRandom {

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){
            System.out.println(getServer());
        }
    }

    public static String getServer(){
        Random random = new Random();

        List<String> ipList = new ArrayList<String>();
        for(String ip : ServerMachines.WEIGHT_SERVER.keySet()){
            Integer weight = ServerMachines.WEIGHT_SERVER.get(ip);
            for(int i = 0; i < weight; i++ ){
                ipList.add(ip);
            }
        }

        int idx = random.nextInt(ipList.size());

        return ipList.get(idx);
    }
}
