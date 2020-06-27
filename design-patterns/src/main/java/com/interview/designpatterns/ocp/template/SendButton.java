package com.interview.designpatterns.ocp.template;

class SendButton extends Button {

    SendButton(int token) {
        super(token);
    }

    @Override
    void onPress() {
        System.out.println("modify some variables, be modified by SendButton");
    }

}
