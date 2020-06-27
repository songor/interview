package com.interview.designpatterns.ocp.strategy;

/**
 * 当 Button 按下的时候，就调用 ButtonServer 的 buttonPressed 方法，事实上是调用 Dialer 实现的 buttonPressed 方法，
 * 这样既完成了 Button 按下的时候执行 Dialer 方法的需求，又不会使 Button 依赖 Dialer。
 * Button 可以扩展复用到其他需要使用 Button 的场景，任何实现 ButtonServer 的类，比如密码锁，都可以使用 Button 而不需要对 Button 代码进行任何修改。
 * 而且 Button 也不需要 switch/case 代码段去判断当前按钮类型，只需要将按钮类型 token 传递给 ButtonServer 就可以了，
 * 这样增加新的按钮类型的时候就不需要修改 Button 代码了。
 */
class Button {

    private int token;

    private ButtonServer buttonServer;

    Button(int token, ButtonServer buttonServer) {
        this.token = token;
        this.buttonServer = buttonServer;
    }

    void press() {
        buttonServer.buttonPressed(token);
    }

}
