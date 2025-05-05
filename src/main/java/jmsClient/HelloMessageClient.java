package jmsClient;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class HelloMessageClient {
    public void sendHelloMessage(ConnectionFactory connectionFactory, Queue messageQueue) throws JMSException{
        try(Connection conn = connectionFactory.createConnection()){
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            TextMessage message = session.createTextMessage("Hello I am here!");
            session.createProducer(messageQueue).send(message);
        }
    }
}
