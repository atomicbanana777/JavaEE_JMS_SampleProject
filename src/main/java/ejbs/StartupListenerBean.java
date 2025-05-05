package ejbs;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.annotation.Resource;
import jakarta.ejb.DependsOn;
import jakarta.ejb.EJB;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Queue;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
@DependsOn({"MessageInbox", "ConsumersContainer"})
public class StartupListenerBean implements ServletContextListener {
    
    @Resource(lookup="jms/activemqConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup="jms/helloMessageQueue") 
    private Queue messageQueue;

    @EJB
    MessageInbox inbox;

    @EJB 
    ConsumersContainer consumersContainer;

    static final Logger logger = Logger.getLogger("StartupListenerBean");

    @Override
    public void contextInitialized(ServletContextEvent sce){
        logger.logp(Level.INFO,"StartupListenerBean", "contextInitialized", "Initializing");
        consumersContainer.createConsumer();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        consumersContainer.closeAll();
        logger.logp(Level.INFO,"StartupListenerBean", "contextDestroyed", "Closing");
    }
}
