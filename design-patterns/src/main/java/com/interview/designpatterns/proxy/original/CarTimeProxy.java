package com.interview.designpatterns.proxy.original;

import com.interview.designpatterns.proxy.Moveable;

import java.time.Duration;
import java.time.LocalTime;

public class CarTimeProxy implements Moveable {

    private Moveable moveable;

    public CarTimeProxy(Moveable moveable) {
        this.moveable = moveable;
    }

    @Override
    public void move() {
        System.out.println("Start write car time");
        LocalTime start = LocalTime.now();
        moveable.move();
        LocalTime end = LocalTime.now();
        Duration duration = Duration.between(start, end);
        System.out.println("End write car time, duration is: " + duration.toMillis());
    }

}
