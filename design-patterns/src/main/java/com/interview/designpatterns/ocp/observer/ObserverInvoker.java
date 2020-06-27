package com.interview.designpatterns.ocp.observer;

class ObserverInvoker {

    public static void main(String[] args) {
        Dialer dialer = new Dialer();
        Button button = new Button(9);
        button.addListener(new DigitButtonDialerAdepter(dialer));
        button.press();

        button = new Button(-99);
        button.addListener(new SendButtonDialerAdepter(dialer));
        button.press();
    }

}
