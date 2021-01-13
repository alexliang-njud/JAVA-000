package JmsActiveMQ.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {
    @JmsListener(destination = "activeTest")
    public void receiveMessage(final String message) {
        System.out.println(message);
    }
}
