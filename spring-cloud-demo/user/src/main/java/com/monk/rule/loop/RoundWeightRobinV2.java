package com.monk.rule.loop;

import com.monk.rule.ServerMachines;

import java.util.Map;

/**
 * 轮询算法V2.0
 */
public class RoundWeightRobinV2 {
    public static void main(String[] args) {
        for(int i = 0; i < 15; i++){
            System.out.println(getServer());
        }
    }

    private static Integer totalWeight(){
        int total = 0;
        for (Integer weight : ServerMachines.WEIGHT_SERVER.values()) {
            total += weight;
        }
        return total;
    }

    public static String getServer(){

        int total = totalWeight();
        total = 0 == total ? 1 : total;
        int position = RequestSequence.getAndIncreatement() % total;

        for(Map.Entry<String, Integer> entry : ServerMachines.WEIGHT_SERVER.entrySet()){
            Integer weight = entry.getValue();
            String ip = entry.getKey();
            if(position < weight){
                return ip;
            }else{
                position = position - weight;
            }
        }
        return null;
    }
}
