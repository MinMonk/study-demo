package com.monk.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerMachines {
    public static final List<String> SERVER_LIST = new ArrayList<String>(10);
    public static final Map<String, Integer> WEIGHT_SERVER = new HashMap<String, Integer>(10);

    static {
        initServers();

        initWeightServers();
    }

    /**
     * 初始化普通server
     */
    public static void initServers() {
        SERVER_LIST.add("192.168.1.1");
        SERVER_LIST.add("192.168.1.2");
        SERVER_LIST.add("192.168.1.3");
        SERVER_LIST.add("192.168.1.4");
        SERVER_LIST.add("192.168.1.5");
        SERVER_LIST.add("192.168.1.6");
        SERVER_LIST.add("192.168.1.7");
        SERVER_LIST.add("192.168.1.8");
        SERVER_LIST.add("192.168.1.9");
        SERVER_LIST.add("192.168.1.10");
    }

    /**
     * 初始化权重server
     */
    public static void initWeightServers() {
        WEIGHT_SERVER.put("192.168.1.1", 5);
        WEIGHT_SERVER.put("192.168.1.2", 1);
        WEIGHT_SERVER.put("192.168.1.3", 1);
        // WEIGHT_SERVER.put("192.168.1.1", 5);
        // WEIGHT_SERVER.put("192.168.1.2", 3);
        // WEIGHT_SERVER.put("192.168.1.3", 2);
        // WEIGHT_SERVER.put("192.168.1.4", 3);
        // WEIGHT_SERVER.put("192.168.1.5", 6);
        // WEIGHT_SERVER.put("192.168.1.6", 1);
        // WEIGHT_SERVER.put("192.168.1.7", 3);
        // WEIGHT_SERVER.put("192.168.1.8", 4);
        // WEIGHT_SERVER.put("192.168.1.9", 2);
        // WEIGHT_SERVER.put("192.168.1.10", 1);
    }
}
