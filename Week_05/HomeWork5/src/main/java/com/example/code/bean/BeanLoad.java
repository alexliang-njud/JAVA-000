package com.example.code.bean;

import com.example.code.bean.auto.AutoWiringExample;
import org.springframework.beans.factory.annotation.Autowired;

public class BeanLoad {
    @Autowired
    public AutoWiringExample instanceExample;

    public static void main(String[] args) {
        BeanLoad beanLoad = new BeanLoad();
        beanLoad.instanceExample.info();
    }
}
