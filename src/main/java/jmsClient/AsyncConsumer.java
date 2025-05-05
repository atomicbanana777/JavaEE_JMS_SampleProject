package jmsClient;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.Session;

public class AsyncConsumer{

    private MessageConsumer consumer;
    Connection connection;

    public AsyncConsumer(ConnectionFactory connectionFactory, Queue messageQueue, MessageListener listener) throws JMSException{  
        connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        consumer = session.createConsumer(messageQueue);
        consumer.setMessageListener(listener);
    }

    public void consume() throws JMSException{
        connection.start();
    }

    public void close() throws JMSException{
        connection.close();
    }

}
