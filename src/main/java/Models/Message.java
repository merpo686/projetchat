package Models;


import java.net.UnknownHostException;
import java.time.*;

public class Message {
    private LocalDateTime date;
    private final User sender;
    private final User receiver;
    private final String data;
    public Message(User sender, User receiver, String messageData) throws UnknownHostException {
        date = LocalDateTime.now();
        this.sender=sender;
        this.receiver=receiver;
        this.data=messageData;
    }
    public String get_message(){return data;}
    public User get_sender(){return sender;}
    public User get_receiver(){return receiver;}
    public LocalDateTime get_date(){return date;}
    public void set_date(LocalDateTime date){
        this.date=date;
    }
    public Boolean equals(Message other){return other.get_date()==this.get_date();}
}
