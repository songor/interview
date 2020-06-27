package com.interview.designpatterns.ocp.template;

import java.util.ArrayList;
import java.util.List;

abstract class Button {

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
        onPress();
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).buttonPressed(token);
        }
    }

    abstract void onPress();

}
