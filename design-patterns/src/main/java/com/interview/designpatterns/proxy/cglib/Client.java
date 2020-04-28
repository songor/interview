package com.interview.designpatterns.proxy.cglib;

public class Client {

    public static void main(String[] args) {
        LogInterceptor interceptor = new LogInterceptor();
        Train t = (Train) interceptor.getProxy(Train.class);
        t.move();
    }

}
