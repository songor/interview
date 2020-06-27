package com.interview.designpatterns.ocp.strategy;

class Dialer implements ButtonServer {

    private static final int SEND_BUTTON = -99;

    @Override
    public void buttonPressed(int token) {
        switch (token) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                enterDigit(token);
                break;
            case SEND_BUTTON:
                dialing();
                break;
            default:
                throw new UnsupportedOperationException("unknown button pressed: token=" + token);
        }
    }

    private void enterDigit(int digit) {
        System.out.println("enter digit: " + digit);
    }

    private void dialing() {
        System.out.println("dialing...");
    }

}
