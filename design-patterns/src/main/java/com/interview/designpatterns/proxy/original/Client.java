package com.interview.designpatterns.proxy.original;

import com.interview.designpatterns.proxy.Car;

public class Client {

    public static void main(String[] args) {
        Car car = new Car();
        CarLogProxy carLogProxy = new CarLogProxy(car);
        CarTimeProxy carTimeProxy = new CarTimeProxy(carLogProxy);
        carTimeProxy.move();
    }

}
