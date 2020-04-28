package com.interview.designpatterns.proxy.cglib;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Train {

    public void move() {
        System.out.println("Train is moving");
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
