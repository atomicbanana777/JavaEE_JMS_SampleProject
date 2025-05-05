package ejbs;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jmsClient.HelloMessageClient;

@Stateless
public class SendMessageBean {

    @Resource(lookup="jms/activemqConnectionFactory")
    private ConnectionFactory connectionFactory;
    
    @Resource(lookup="jms/helloMessageQueue") 
    private Queue messageQueue;

    public void sendMessage() throws JMSException{
        HelloMessageClient client = new HelloMessageClient();
        client.sendHelloMessage(connectionFactory, messageQueue);
    }

}
