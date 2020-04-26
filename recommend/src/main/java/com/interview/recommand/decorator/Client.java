package com.interview.recommand.decorator;

public class Client {

    public static void main(String[] args) {
        Animal jerry = new Rat();
        jerry = new DecorateAnimal(jerry, FlyFeature.class);
        jerry = new DecorateAnimal(jerry, DigFeature.class);
        jerry.doStuff();
    }

}
