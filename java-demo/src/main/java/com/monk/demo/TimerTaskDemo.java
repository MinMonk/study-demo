/**
 * 
 * 文件名：TimerTask.java
 * 版权： Copyright 2017-2022 Monk All Rights Reserved.
 * 描述： Monk学习使用
 */
package com.monk.demo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Monk
 * @version V1.0
 * @date 2019年6月3日 上午11:45:51
 */
public class TimerTaskDemo {

    private static Date startDate = null;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                long currTime = System.currentTimeMillis();
                if (startDate == null) {
                    startDate = new Date(currTime);
                }

                System.out.println("process data.... startDate = " + sdf.format(startDate) + ", now = "
                        + sdf.format(new Date(currTime)));
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.SECOND, 3);
                if (cal.getTime().getTime() <= currTime) {
                    System.out.println("process db data. startDate = " + sdf.format(startDate) + ", now = "
                            + sdf.format(new Date(currTime)));
                    startDate = new Date(currTime);
                }

            }
        }, 1000L, 5000L);
    }

}
