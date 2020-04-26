package com.interview.recommand.concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierDemo {

    static class Worker implements Runnable {

        private CyclicBarrier cb;

        public Worker(CyclicBarrier cb) {
            this.cb = cb;
        }

        public void run() {
            try {
                Thread.sleep(new Random().nextInt(1000));
                cb.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        CyclicBarrier cb = new CyclicBarrier(2, new Runnable() {
            public void run() {
                System.out.println("隧道已经打通");
            }
        });
        new Thread(new Worker(cb), "工人 1").start();
        new Thread(new Worker(cb), "工人 2").start();
    }

}
