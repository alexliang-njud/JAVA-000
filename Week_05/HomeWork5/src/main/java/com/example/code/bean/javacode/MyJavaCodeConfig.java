package com.example.code.bean.javacode;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyJavaCodeConfig {
    @Bean
    public MyJavaCodeExample javaCodeExample() {
        return new MyJavaCodeExample();
    }
}
