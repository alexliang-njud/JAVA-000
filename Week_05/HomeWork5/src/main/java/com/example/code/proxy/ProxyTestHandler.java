package com.example.code.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ProxyTestHandler implements InvocationHandler {
    private Object object;

    public ProxyTestHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        method.invoke(object, args);
        after();
        return null;
    }

    private void before() {
        System.out.println("proxy before method, hello");
    }

    private void after() {
        System.out.println("proxy after method, end");
    }
}
