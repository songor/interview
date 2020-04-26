package com.interview.recommand.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CountDownLatchDemo {

    static class Runner implements Callable<Integer> {

        private CountDownLatch begin;

        private CountDownLatch end;

        public Runner(CountDownLatch begin, CountDownLatch end) {
            this.begin = begin;
            this.end = end;
        }

        public Integer call() throws Exception {
            int score = new Random().nextInt(25);
            begin.await();
            TimeUnit.MILLISECONDS.sleep(score);
            end.countDown();
            return score;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int num = 10;
        CountDownLatch begin = new CountDownLatch(1);
        CountDownLatch end = new CountDownLatch(num);
        ExecutorService es = Executors.newFixedThreadPool(num);
        List<Future<Integer>> futures = new ArrayList<Future<Integer>>(10);
        for (int i = 0; i < 10; i++) {
            futures.add(es.submit(new Runner(begin, end)));
        }
        begin.countDown();
        end.await();
        int count = 0;
        for (Future<Integer> future : futures) {
            count += future.get();
        }
        System.out.println("平均分数为：" + count / num);
    }

}
