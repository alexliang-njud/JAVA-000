package JmsActiveMQ.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JmsProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(final String topic, final Map messageMap) {
        String message = messageMap.toString();
        jmsTemplate.convertAndSend(topic, message);
    }
}
