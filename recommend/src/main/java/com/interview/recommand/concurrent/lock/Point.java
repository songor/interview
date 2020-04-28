package com.interview.recommand.concurrent.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock
 * <p>
 * 所有获取锁的方法，都返回一个 stamp
 * 所有释放锁的方法，都需要一个 stamp，这个 stamp 必须是和成功获取锁时得到的 stamp 一致
 * StampedLock 是不可重入的（可能会造成死锁）
 * StampedLock 有三种模式：writeLock()、readLock()、tryOptimisticRead()
 * StampedLock 支持读锁和写锁的相互转换（ReentrantReadWriteLock 读锁不能升级为写锁）
 */
public class Point {

    private double x, y;
    private final StampedLock lock = new StampedLock();

    public void move(double x, double y) {
        // 写锁
        long stamp = lock.writeLock();
        try {
            this.x += x;
            this.y += y;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {
        // 乐观读锁
        long stamp = lock.tryOptimisticRead();
        double x = this.x, y = this.y;
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                x = this.x;
                y = this.y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(x * x + y * y);
    }

    public void moveIfAtOrigin(double x, double y) {
        long stamp = lock.tryOptimisticRead();
        try {
            while (this.x == 0.0 && this.y == 0.0) {
                // 读锁转换为写锁
                long ws = lock.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    stamp = ws;
                    this.x = x;
                    this.y = y;
                    break;
                } else {
                    lock.unlockRead(stamp);
                    stamp = lock.writeLock();
                }
            }
        } finally {
            lock.unlock(stamp);
        }
    }

}
