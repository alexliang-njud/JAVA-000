package com.example.code.bean.auto;

import org.springframework.stereotype.Component;

/**
 * 自动注解，bean装配
 */
@Component
public class AutoWiringExample {
    public AutoWiringExample() {
        System.out.println("Construct Example");
    }

    public void info() {
        System.out.println("Auto wiring example");
    }
}
