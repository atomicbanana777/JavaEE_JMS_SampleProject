package jmsClient;

import java.util.logging.Level;
import java.util.logging.Logger;

import ejbs.MessageInbox;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

public class HelloQueueListener implements MessageListener {

    private static Logger logger = Logger.getLogger("HelloQueueListener");
    private MessageInbox inbox;
    public HelloQueueListener(MessageInbox inbox){
        this.inbox = inbox;
    }

    @Override
    public void onMessage(Message message) {
       TextMessage msg = (TextMessage) message;
       try{
            String text = msg.getText();
            logger.logp(Level.INFO, "HelloQueueListener", "onMessage", "Message: {0}", text );
            inbox.addMessage(text);
       } catch(JMSException e){
            e.printStackTrace();
       }
    }
    
}
