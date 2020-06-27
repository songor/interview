package com.interview.designpatterns.ocp.template;

class TemplateInvoker {

    public static void main(String[] args) {
        Dialer dialer = new Dialer();
        Button button = new SendButton(9);
        button.addListener(new DigitButtonDialerAdepter(dialer));
        button.press();

        button = new SendButton(-99);
        button.addListener(new SendButtonDialerAdepter(dialer));
        button.press();
    }

}
