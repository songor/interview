package com.interview.designpatterns.proxy.jdk;

import com.interview.designpatterns.proxy.Car;
import com.interview.designpatterns.proxy.Moveable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Client {

    public static void main(String[] args) {
        Car car = new Car();
        InvocationHandler time = new TimeInvocationHandler(car);
        Moveable moveable = (Moveable) Proxy.newProxyInstance(car.getClass().getClassLoader(), car.getClass().getInterfaces(), time);

        InvocationHandler log = new LogInvocationHandler(moveable);
        Moveable m = (Moveable) Proxy.newProxyInstance(moveable.getClass().getClassLoader(), moveable.getClass().getInterfaces(), log);
        m.move();
    }

}
