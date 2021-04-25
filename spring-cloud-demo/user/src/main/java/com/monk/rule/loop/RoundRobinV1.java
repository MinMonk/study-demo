package com.monk.rule.loop;

import com.monk.rule.ServerMachines;

/**
 * 轮询算法V1.0
 */
public class RoundRobinV1 {

    public static Integer position = 0;


    public static void main(String[] args) {
        for(int i = 0; i < 15; i++){
            System.out.println(getServer());
        }
    }

    public static String getServer(){
        String ip = "";
        synchronized (position){
            if(position >= ServerMachines.SERVER_LIST.size()){
                position = 0;
            }
            ip = ServerMachines.SERVER_LIST.get(position);
            position++;
        }
        return ip;
    }
}
