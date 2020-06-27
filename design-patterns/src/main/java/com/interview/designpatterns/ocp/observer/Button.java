package com.interview.designpatterns.ocp.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过 addListener 我们可以添加多个需要观察按钮按下事件的监听者实现，当按钮需要控制新设备的时候，
 * 只需要将实现了 ButtonListener 的设备实现添加到 Button 的 List 列表就可以了。
 * 被观察者和观察者通过 Listener 接口解耦合，观察者（的适配器）通过调用被观察者的 addListener 方法将自己添加到观察列表，
 * 当观察行为发生时，被观察者会逐个遍历 Listener List 通知观察者。
 */
class Button {

    private int token;

    private List<ButtonListener> listeners;

    Button(int token) {
        this.token = token;
        this.listeners = new ArrayList<>();
    }

    void addListener(ButtonListener buttonListener) {
        listeners.add(buttonListener);
    }

    void press() {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).buttonPressed(token);
        }
    }

}
