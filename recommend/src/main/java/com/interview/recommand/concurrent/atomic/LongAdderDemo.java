package com.interview.recommand.concurrent.atomic;

/**
 * AtomicLong：采用自旋的方式不断更新目标值，直到更新成功
 * 在并发量较低的环境下，线程冲突的概率比较小，自旋的次数不会很多
 * 但是，高并发环境下，N 个线程同时进行自旋操作，会出现大量失败并不断自旋的情况，此时 AtomicLong 的自旋会成为瓶颈
 * LongAdder：解决高并发环境下 AtomicLong 的自旋瓶颈问题
 * <p>
 * AtomicLong 中有个内部变量 value 保存着实际的 long 值，所有的操作都是针对该变量进行
 * 也就是说，高并发环境下，value 变量其实是一个热点，也就是 N 个线程竞争一个热点
 * LongAdder 的基本思路就是分散热点，将 value 值分散到一个数组中，不同线程会命中到数组的不同槽中，各个线程只对自己槽中的那个值进行 CAS 操作，这样热点就被分散了，冲突的概率就小很多
 * 如果要获取真正的 long 值，只要将各个槽中的变量值累加返回
 * volatile long base，volatile Cell[] cells
 * <p>
 * AtomicLong 提供的功能其实更丰富，尤其是 addAndGet、decrementAndGet、compareAndSet 这些方法
 * addAndGet、decrementAndGet 除了单纯地做自增自减外，还可以立即获取增减后的值，而 LongAdder 则需要做同步控制才能精确获取增减后的值
 */
public class LongAdderDemo {
}
