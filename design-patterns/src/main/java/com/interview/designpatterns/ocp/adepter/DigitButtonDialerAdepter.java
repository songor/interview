package com.interview.designpatterns.ocp.adepter;

class DigitButtonDialerAdepter implements ButtonServer {

    private Dialer dialer;

    DigitButtonDialerAdepter(Dialer dialer) {
        this.dialer = dialer;
    }

    @Override
    public void buttonPressed(int token) {
        dialer.enterDigit(token);
    }

}
