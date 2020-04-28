package com.interview.designpatterns.proxy.jdk;

import com.interview.designpatterns.proxy.Moveable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalTime;

public class TimeInvocationHandler implements InvocationHandler {

    private Moveable moveable;

    public TimeInvocationHandler(Moveable moveable) {
        this.moveable = moveable;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Start write time");
        LocalTime start = LocalTime.now();
        method.invoke(moveable);
        LocalTime end = LocalTime.now();
        Duration duration = Duration.between(start, end);
        System.out.println("End write time, duration is: " + duration.toMillis());
        return null;
    }
}
