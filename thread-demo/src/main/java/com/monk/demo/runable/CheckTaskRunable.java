package com.monk.demo.runable;

import java.util.List;

public class CheckTaskRunable implements Runnable{

    private List<String> workData;

    public CheckTaskRunable(List<String> workData) {
        this.workData = workData;
    }

    @Override
    public void run() {
        String currThreadName = Thread.currentThread().getName();
        System.out.println(currThreadName + "工作数据为" + workData);
        for (String str : workData) {
            //System.out.println(currThreadName + ": " + str);
        }
        System.out.println(currThreadName + "执行完毕");
    }
}
