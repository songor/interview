package com.interview.designpatterns.ocp.template;

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
