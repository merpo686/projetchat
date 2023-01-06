package Models;


import java.net.UnknownHostException;
import java.sql.Date;

public class Message {
    private Date date;
    private final User sender;
    private final User receiver;
    private final String data;
    public Message(User sender, User receiver, String messageData) throws UnknownHostException {
        long millis = System.currentTimeMillis();
        date = new Date(millis);
        this.sender=sender;
        this.receiver=receiver;
        this.data=messageData;
    }
    public String get_message(){return data;}
    public User get_sender(){return sender;}
    public User get_receiver(){return receiver;}
    public Date get_date(){return date;}
    public void set_date(Date date){
        this.date=date;
    }
    public Boolean equals(Message other){return other.get_date()==this.get_date();}
}
