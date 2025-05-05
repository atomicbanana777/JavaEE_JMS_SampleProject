package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import ejbs.MessageInbox;
import jakarta.ejb.DependsOn;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@DependsOn("MessageInbox")
@WebServlet("/inbox")
public class MessageInboxServlet extends HttpServlet {
    @EJB
    MessageInbox messageInbox;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        PrintWriter out = response.getWriter();

        List<String> messages = messageInbox.getMessages();
        if(messages.isEmpty()){
            out.println("No messages....");
        } else {
            for(String s : messages){
                out.println(s);
            }
        }
    }
}
