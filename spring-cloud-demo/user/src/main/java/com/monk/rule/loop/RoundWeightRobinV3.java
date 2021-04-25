package com.monk.rule.loop;

import com.monk.rule.ServerMachines;

import java.util.HashMap;
import java.util.Map;

/**
 * 轮询算法V3.0
 */
public class RoundWeightRobinV3 {

    private static Map<String, Weight> weightMap = new HashMap<String, Weight>();

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){
            System.out.println(getServer());
        }
    }

    public static String getServer(){
        initData();

        Integer total = totalWeight();

        Weight maxWeight = getMaxWeight();
        maxWeight.setCurrWeight(maxWeight.getCurrWeight() - total);

        for(Weight weight : weightMap.values()){
            weight.setCurrWeight(weight.getCurrWeight() + weight.getWeight());
        }

        return maxWeight.getIp();
    }

    private static Integer totalWeight(){
        int total = 0;
        for (Integer weight : ServerMachines.WEIGHT_SERVER.values()) {
            total += weight;
        }
        return total;
    }


    private static void initData(){
        if(weightMap.isEmpty()){
            for(String ip : ServerMachines.WEIGHT_SERVER.keySet()){
                Integer weight = ServerMachines.WEIGHT_SERVER.get(ip);
                weightMap.put(ip, new Weight(weight, weight, ip));
            }
        }
    }

    private static Weight getMaxWeight(){
        Weight maxWeight = null;
        for(Weight weight : weightMap.values()){
            if(null == maxWeight || weight.getCurrWeight() > maxWeight.getCurrWeight()){
                maxWeight = weight;
            }
        }
        return maxWeight;
    }
}
