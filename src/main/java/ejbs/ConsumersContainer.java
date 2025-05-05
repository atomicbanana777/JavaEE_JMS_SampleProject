package ejbs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.Resource;
import jakarta.ejb.DependsOn;
import jakarta.ejb.EJB;
import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import jmsClient.AsyncConsumer;
import jmsClient.HelloQueueListener;

@Singleton
@DependsOn("MessageInbox")
public class ConsumersContainer {

    @Resource(lookup="jms/activemqConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(lookup="jms/helloMessageQueue") 
    private Queue messageQueue;
    @EJB
    MessageInbox inbox;
    @Resource(name="numOfConsumer")
    private int numberOfConsumer;

    private static Logger logger = Logger.getLogger("ConsumersContainer");
    private List<AsyncConsumer> consumers = new ArrayList<>();

    public void addConsumer(AsyncConsumer consumer){
        consumers.add(consumer);
    }

    public List<AsyncConsumer> getConsumers(){
        return consumers;
    }

    public void startAll(){
        Iterator<AsyncConsumer> consumersIterator = consumers.iterator();
        while(consumersIterator.hasNext()){
            AsyncConsumer consumer = consumersIterator.next();
            try {
                consumer.consume();
            } catch (JMSException e) {
                consumersIterator.remove();
                logger.logp(Level.WARNING, "ConsumersContainer", "startALL",
                    "Failed to start consumer, remove it from consumers container, error message: {0}",
                         e.getMessage());
            }
        }
    }

    public void closeAll(){
        Iterator<AsyncConsumer> consumersIterator = consumers.iterator();
        while(consumersIterator.hasNext()){
            AsyncConsumer consumer = consumersIterator.next();
            try {
                consumer.close();
            } catch (JMSException e) {
                consumersIterator.remove();
                logger.logp(Level.WARNING, "ConsumersContainer", "closeAll",
                "Failed to close consumer, remove it from consumers container, error message: {0}",
                     e.getMessage());
            }
        }
    }

    //@Schedule(hour="*", minute="*", second="*/10", persistent=false)
    public void checkConsumersStatus(){
        startAll();
        if(consumers.size() < numberOfConsumer){
            logger.logp(Level.WARNING, "ConsumersContainer", "checkConsumersStatus",
             "Number of consumers is {0} less than {1}", new Integer[]{consumers.size(), numberOfConsumer});
             createConsumer();
        }
    }

    public void createConsumer(){
        while(consumers.size() < numberOfConsumer){
            AsyncConsumer consumer;
            try {
                consumer = new AsyncConsumer(connectionFactory, messageQueue, new HelloQueueListener(inbox));
                addConsumer(consumer);
                logger.logp(Level.INFO, "ConsumersContainer", "createConsumer", "consumer created, Number of consumers is {0}", consumers.size());
            } catch (JMSException e) {
                logger.logp(Level.WARNING, "ConsumersContainer", "createConsumer", "Failed to create consumer with error message: {0}",
                e.getMessage());
                break;
            }
        }
        startAll();
    }
}
