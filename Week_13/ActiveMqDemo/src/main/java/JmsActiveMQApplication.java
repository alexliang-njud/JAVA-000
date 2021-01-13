import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;
import JmsActiveMQ.producer.JmsProducer;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan(basePackages = "JmsActiveMQ")
@EnableJms
@Slf4j
public class JmsActiveMQApplication implements ApplicationRunner {
    @Autowired
    private JmsProducer producer;

    public static void main(String[] args) {
        SpringApplication.run(JmsActiveMQApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        String topic = "activeTest";
        Map<String, String> message = new HashMap<>(1);
        message.put("testUser", "lxd-njud");
        log.debug("send message to topic " + topic + " :: " + message);
        producer.sendMessage(topic, message);
    }
}
