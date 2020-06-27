package com.interview.designpatterns.ocp.template;

class DigitButtonDialerAdepter implements ButtonListener {

    private Dialer dialer;

    DigitButtonDialerAdepter(Dialer dialer) {
        this.dialer = dialer;
    }

    @Override
    public void buttonPressed(int token) {
        dialer.enterDigit(token);
    }

}
