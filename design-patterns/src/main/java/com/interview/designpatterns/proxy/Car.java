package com.interview.designpatterns.proxy;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Car implements Moveable {

    @Override
    public void move() {
        System.out.println("Car is moving");
        try {
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
