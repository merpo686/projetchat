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
    public String getMessage(){return data;}
    public User getSender(){return sender;}
    public User getReceiver(){return receiver;}
    public Date getDate(){return date;}
    public void setDate(Date date){
        this.date=date;
    }
    public Boolean equals(Message other){return other.getDate()==this.getDate();}
    public String toString(){return "Sender:"+ getSender()+" Receiver:"+ getReceiver()+
            " Date:"+ getDate()+" Message:"+ getMessage();}
}
