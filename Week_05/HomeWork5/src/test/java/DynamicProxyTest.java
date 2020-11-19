
import com.example.code.proxy.HelloService;
import com.example.code.proxy.HelloServiceImpl;
import com.example.code.proxy.ProxyTestHandler;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class DynamicProxyTest {
    @Test
    public void test() {
        HelloServiceImpl exampleService = new HelloServiceImpl();
        ClassLoader classLoader = exampleService.getClass().getClassLoader();
        Class[] interfaces = exampleService.getClass().getInterfaces();
        ProxyTestHandler handler = new ProxyTestHandler(exampleService);

        HelloService proxy = (HelloService) Proxy.newProxyInstance(classLoader, interfaces, handler);
        proxy.hello();
    }
}
