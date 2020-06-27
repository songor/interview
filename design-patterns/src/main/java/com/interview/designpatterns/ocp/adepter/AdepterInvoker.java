package com.interview.designpatterns.ocp.adepter;

/**
 * 不要由 Dialer 类直接实现 ButtonServer 接口，而是增加两个适配器 DigitButtonDialerAdapter、SendButtonDialerAdapter，
 * 由适配器实现 ButtonServer 接口，在适配器的 buttonPressed 方法中调用 Dialer 的 enterDigit 方法和 dialing 方法，
 * 而 Dialer 类保持不变，Dialer 类实现开闭原则。
 */
class AdepterInvoker {

    public static void main(String[] args) {
        Dialer dialer = new Dialer();
        new Button(9, new DigitButtonDialerAdepter(dialer)).press();
        new Button(-99, new SendButtonDialerAdepter(dialer)).press();
    }

}
