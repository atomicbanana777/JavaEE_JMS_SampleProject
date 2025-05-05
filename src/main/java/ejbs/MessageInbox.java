package ejbs;

import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.Singleton;

@Singleton
public class MessageInbox {
    private List<String> messages = new ArrayList<>();

    public List<String> getMessages(){
        return messages;
    }

    public void addMessage(String msg){
        messages.add(msg);
    }
}
