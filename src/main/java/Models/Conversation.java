package Models;

import Managers.ActiveUserManager;

import java.util.ArrayList;

public class Conversation {
    private static ArrayList<Message> listMessages;
    static Conversation instance;

    public Conversation(User dest){
        listMessages = new ArrayList<Message>();
    }

    public static Conversation getInstance(User userDest) {
        if (instance == null) {
            instance = new Conversation(userDest);
        }
        return instance;
    }

    public void addMessage(Message msg){
        listMessages.add(msg);
    }

    public String getHistory(){
        return listMessages.toString();
    }
}
