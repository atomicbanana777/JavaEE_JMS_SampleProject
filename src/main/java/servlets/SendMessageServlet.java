package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import ejbs.SendMessageBean;
import jakarta.ejb.EJB;
import jakarta.jms.JMSException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/send")
public class SendMessageServlet extends HttpServlet {
    @EJB
    SendMessageBean client;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            client.sendMessage();
            PrintWriter out = response.getWriter();
            out.println("Message Sent!!!");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
