package com.interview.recommand.decorator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class DecorateAnimal implements Animal {

    private Animal animal;

    private Class<? extends Feature> clz;

    public DecorateAnimal(Animal animal, Class<? extends Feature> clz) {
        this.animal = animal;
        this.clz = clz;
    }

    public void doStuff() {
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Object obj = null;
                if (Modifier.isPublic(method.getModifiers())) {
                    obj = method.invoke(clz.newInstance(), args);
                }
                animal.doStuff();
                return obj;
            }
        };
        ClassLoader loader = getClass().getClassLoader();
        Feature proxy = (Feature) Proxy.newProxyInstance(loader, clz.getInterfaces(), handler);
        proxy.load();
    }

}
