package com.interview.designpatterns.proxy.original;

import com.interview.designpatterns.proxy.Moveable;

public class CarLogProxy implements Moveable {

    private Moveable moveable;

    public CarLogProxy(Moveable moveable) {
        this.moveable = moveable;
    }

    @Override
    public void move() {
        System.out.println("Start write car record");
        moveable.move();
        System.out.println("End write car record");
    }

}
