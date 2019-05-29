package com.paradise.timer;

import java.util.Timer;

/**
 * @author Paradise
 */
public class TimerTask {

    Timer timer = new Timer();

    public static void main(String[] args) {
        TimerTask timerTask = new TimerTask();
        timerTask.run();
    }

    public void run() {
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                long l = System.currentTimeMillis();
                System.out.println(">>> Here we go!");
                while (true) {
                    if ((System.currentTimeMillis() - l) > 15000) {
                        System.out.println(">>> Here we out!");
                        break;
                    }
                }
            }
        }, 1000, 10000);
    }

}
