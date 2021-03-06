package com.linyimin.java.concurrency.chapter4;

import java.util.concurrent.TimeUnit;

/**
 * @Author: linyimin
 * @Description:
 * @Date: Created in 上午10:21 18-6-26
 */
public class ThreadState {
    public static void main(String[] args){
        new Thread(new TimeWaiting (), "TimeWaitingThread").start();
        new Thread(new Waiting(), "WaitingThread").start();
        // 使用两个Blocked线程,一个获取锁成功,另一个被阻塞
        new Thread(new Blocked(), "BlockedThread-1").start();
        new Thread(new Blocked(), "BlockedThread-2").start();
    }

    static class TimeWaiting implements Runnable{

        @Override
        public void run() {
            while (true){
                sleep(1);
            }
        }
    }

    static class Waiting implements Runnable{

        @Override
        public void run() {
            while (true){
                synchronized (Waiting.class){
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Blocked implements Runnable {
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    sleep(1);
                }
            }
        }
    }

    public static void sleep(long time){
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
