package Models;

import Managers.ActiveUserManager;

import java.util.ArrayList;

public class Conversation {
    private final ArrayList<Message> listMessages;
    private final User destination;
    public Conversation(User dest){
        listMessages = new ArrayList<Message>();
        this.destination = dest;
    }
    public void addMessage(Message msg){
        listMessages.add(msg);
    }
    public User getDestination(){return this.destination;}
    public String getHistory(){
        return listMessages.toString();
    }
    public ArrayList<Message> getListMessages(){return listMessages;}
    public Message getlastMessage(){return listMessages.get(listMessages.size()-1);}
}
