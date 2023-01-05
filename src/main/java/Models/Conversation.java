package Models;

import Managers.ActiveUserManager;

import java.util.ArrayList;

public class Conversation {
    private final ArrayList<Message> listMessages;
    private final String hostname;
    public Conversation(String hostname){
        listMessages = new ArrayList<Message>();
        this.hostname = hostname;
    }
    public void addMessage(Message msg){
        listMessages.add(msg);
    }
    public String getDestination(){return this.hostname;}
    public String getHistory(){
        return listMessages.toString();
    }
    public ArrayList<Message> getListMessages(){return listMessages;}
    public Message getlastMessage(){return listMessages.get(listMessages.size()-1);}
}
