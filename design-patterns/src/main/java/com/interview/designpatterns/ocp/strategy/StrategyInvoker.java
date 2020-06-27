package com.interview.designpatterns.ocp.strategy;

class StrategyInvoker {

    public static void main(String[] args) {
        Dialer dialer = new Dialer();
        new Button(9, dialer).press();
        new Button(-99, dialer).press();
    }

}
