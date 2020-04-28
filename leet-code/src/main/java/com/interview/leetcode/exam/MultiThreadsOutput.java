package com.interview.leetcode.exam;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * 定义3个线程 A，B，C
 * A线程负责输出“A”
 * B线程负责输出“B”
 * C线程负责输出“C”
 * <p>
 * 要求：
 * 每个线程循环输出10次
 * <p>
 * 最终满足输出结果：ABC三个线程输出的结果交替出现
 * 例如：ABCABCABC...
 */
public class MultiThreadsOutput {

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();

        Runnable a = () -> {
            IntStream.range(0, 10).forEach((i) -> {
                lock.lock();
                try {
                    System.out.print("A");
                    condition2.signal();
                    condition1.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });
        };

        Runnable b = () -> {
            IntStream.range(0, 10).forEach((i) -> {
                lock.lock();
                try {
                    System.out.print("B");
                    condition3.signal();
                    condition2.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });
        };

        Runnable c = () -> {
            IntStream.range(0, 10).forEach((i) -> {
                lock.lock();
                try {
                    System.out.print("C");
                    condition1.signal();
                    condition3.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            });
        };

        new Thread(a).start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(b).start();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(c).start();
    }

}
