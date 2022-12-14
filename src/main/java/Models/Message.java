package Models;

import Managers.UserMain;

import java.net.UnknownHostException;
import java.util.Date;

public class Message extends Notifications {
    private String data;
    private Date date;
    public Message(User user, String messageData) throws UnknownHostException {
        super(user);
        this.data = messageData;
        this.date = new Date();
    }
}
