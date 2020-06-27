package com.interview.designpatterns.ocp.rotten;

class RottenInvoker {

    public static void main(String[] args) {
        Dialer dialer = new Dialer();
        new Button(9, dialer).press();
        new Button(-99, dialer).press();
    }

}
