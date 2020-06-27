package com.interview.designpatterns.ocp.rotten;

/**
 * 当我们想要增加按钮类型的时候，比如，当我们需要按钮支持星号（*）和井号（#）的时候，我们必须修改 Button 类代码；
 * 当我们想要用这个按钮控制一个密码锁而不是拨号器的时候，因为按钮关联了拨号器，所以依然要修改 Button 类代码；
 * 当我们想要按钮控制多个设备的时候，还是要修改 Button 类代码。
 */
class Button {

    private static final int SEND_BUTTON = -99;

    private Dialer dialer;

    private int token;

    Button(int token, Dialer dialer) {
        this.token = token;
        this.dialer = dialer;
    }

    void press() {
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
                dialer.enterDigit(token);
                break;
            case SEND_BUTTON:
                dialer.dialing();
                break;
            default:
                throw new UnsupportedOperationException("unknown button pressed: token=" + token);
        }
    }

}
