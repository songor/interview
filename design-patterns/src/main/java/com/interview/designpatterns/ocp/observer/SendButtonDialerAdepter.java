package com.interview.designpatterns.ocp.observer;

class SendButtonDialerAdepter implements ButtonListener {

    private Dialer dialer;

    SendButtonDialerAdepter(Dialer dialer) {
        this.dialer = dialer;
    }

    @Override
    public void buttonPressed(int token) {
        dialer.dialing();
    }

}
