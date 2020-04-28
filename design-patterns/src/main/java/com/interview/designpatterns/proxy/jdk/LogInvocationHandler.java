package com.interview.designpatterns.proxy.jdk;


import com.interview.designpatterns.proxy.Moveable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogInvocationHandler implements InvocationHandler {

    private Moveable moveable;

    public LogInvocationHandler(Moveable moveable) {
        this.moveable = moveable;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("Start write record");
        method.invoke(moveable);
        System.out.println("End write record");
        return null;
    }


}
