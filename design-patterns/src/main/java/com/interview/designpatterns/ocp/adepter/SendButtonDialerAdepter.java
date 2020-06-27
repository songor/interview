package com.interview.designpatterns.ocp.adepter;

class SendButtonDialerAdepter implements ButtonServer {

    private Dialer dialer;

    SendButtonDialerAdepter(Dialer dialer) {
        this.dialer = dialer;
    }

    @Override
    public void buttonPressed(int token) {
        dialer.dialing();
    }

}
