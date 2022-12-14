package Models;

import Managers.ActiveUserManager;

import java.util.ArrayList;

public class Conversation {
    private static ArrayList<Message> listMessages;
    static Conversation instance;
    private User dest;

    public Conversation(User dest){
        this.dest=dest;
        this.listMessages = new ArrayList<Message>();
    }

    public static Conversation getInstance(User userDest) {
        if (instance == null) {
            instance = new Conversation(userDest);
        }
        return instance;
    }

    public void addMessage(Message msg){
        this.listMessages.add(msg);
    }

    public String getHistory(){
        return this.listMessages.toString();
    }
}
