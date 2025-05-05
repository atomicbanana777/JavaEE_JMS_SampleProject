package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import jakarta.annotation.Resource;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/browser")
public class MessageBrowserServlet extends HttpServlet {

    @Resource(lookup="jms/openmqConnectionFactory")
    private ConnectionFactory connectionFactory_openMQ;
    @Resource(lookup="jms/openmqMessageQueue") 
    private Queue queue_openMQ;

    @Resource(lookup="jms/activemqConnectionFactory")
    private ConnectionFactory connectionFactory_activeMQ;
    @Resource(lookup="jms/helloMessageQueue") 
    private Queue queue_activeMQ;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();
        QueueBrowser browser;
        try(Connection conn = connectionFactory_openMQ.createConnection()){
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            browser = session.createBrowser(queue_openMQ);
            Enumeration msgs = browser.getEnumeration();

            if (!msgs.hasMoreElements()) {
                out.println("No messages in queue openMQ");
            } else {
                while (msgs.hasMoreElements()) {
                    Message tempMsg = (Message) msgs.nextElement();
                    out.println("\nopenMQ Message: " + tempMsg);
                }
            }
            
        }catch (JMSException e) {
            e.printStackTrace();
        }

        try(Connection conn = connectionFactory_activeMQ.createConnection()){
            conn.start();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            browser = session.createBrowser(queue_activeMQ);
            Enumeration msgs = browser.getEnumeration();

            if (!msgs.hasMoreElements()) {
                out.println("No messages in queue activeMQ");
            } else {
                while (msgs.hasMoreElements()) {
                    Message tempMsg = (Message) msgs.nextElement();
                    out.println("\nactiveMQ Message: " + tempMsg);
                }
            }
            
        }catch (JMSException e) {
            e.printStackTrace();
        }

    }

}
