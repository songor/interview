package com.interview.designpatterns.ocp.adepter;

class Button {

    private int token;

    private ButtonServer buttonServer;

    Button(int token, ButtonServer buttonServer) {
        this.token = token;
        this.buttonServer = buttonServer;
    }

    void press() {
        buttonServer.buttonPressed(token);
    }

}
