package bean;

import com.example.code.bean.javacode.MyJavaCodeConfig;
import com.example.code.bean.javacode.MyJavaCodeExample;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyJavaCodeExampleTest {
    @Test
    public void test() {
        AnnotationConfigApplicationContext configApplicationContext =
                new AnnotationConfigApplicationContext(MyJavaCodeConfig.class);
        MyJavaCodeExample example = (MyJavaCodeExample) configApplicationContext.getBean("javaCodeExample");
        example.info();
    }
}
