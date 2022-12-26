package Models;


import java.net.UnknownHostException;
import java.util.Date;

public class Message extends Notifications {
    public Message(User user, String messageData) throws UnknownHostException {
        super(user);
        Date date = new Date();
    }
}
