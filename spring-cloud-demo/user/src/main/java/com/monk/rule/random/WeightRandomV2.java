package com.monk.rule.random;

import com.monk.rule.ServerMachines;

import java.util.*;

/**
 * 随机算法-版本V3
 * 权重算法的优化版本
 *  优化点:V2的版本中,如果服务器的数量过多or权重数值很大(dubbo默认的都是几百几百的权重),
 *  新copy的数组会占用服务器内存资源,产生不必要的开销从而影响性能
 *
 * 相对V2版本的随机算法,优化点有以下:
 * 1. 通过计算随机数所在的区间从而推出对应的服务器
 * 2. 对于权重设置的都一样,就没必要进行计算,直接按照普通的随机算法推出对应的服务器即可
 */
public class WeightRandomV2 {

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){
            System.out.println(getServer());
        }
    }

    public static String getServer(){

        int total = 0;
        Set<Integer> weightSet = new HashSet<Integer>();
        for (Integer weight : ServerMachines.WEIGHT_SERVER.values()) {
            total += weight;

            weightSet.add(weight);
        }

        // 这里用一个set而不直接用boolean类型的值来判断,是为了避免个别的权重一样,但是其他的都不一样的情况
        boolean sameWeight = true;
        if(weightSet.size() > 1){
            sameWeight = false;
        }

        Random random = new Random();
        if(sameWeight){
            // 如果全部服务器的权重都设置的一样,那就随机调用一个即可,没有必要进行复杂的计算
            int idx = random.nextInt(ServerMachines.WEIGHT_SERVER.size());
            return (String) ServerMachines.WEIGHT_SERVER.keySet().toArray()[idx];
        }else{

            int idx = random.nextInt(total);
            for(Map.Entry<String, Integer> entry : ServerMachines.WEIGHT_SERVER.entrySet()){
                Integer weight = entry.getValue();
                String ip = entry.getKey();
                if(idx < weight){
                    return ip;
                }else{
                    idx = idx - weight;
                }
            }
        }

        return null;
    }
}
