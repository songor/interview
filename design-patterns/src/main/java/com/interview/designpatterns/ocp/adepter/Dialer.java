package com.interview.designpatterns.ocp.adepter;

class Dialer {

    void enterDigit(int digit) {
        System.out.println("enter digit: " + digit);
    }

    void dialing() {
        System.out.println("dialing...");
    }

}
