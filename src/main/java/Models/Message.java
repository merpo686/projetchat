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
<<<<<<< HEAD
    public String get_message(){return data;}
    public User get_sender(){return sender;}
    public User get_receiver(){return receiver;}
    public LocalDateTime get_date(){return date;}
    public void set_date(LocalDateTime date){
=======
    public String getMessage(){return data;}
    public User getSender(){return sender;}
    public User getReceiver(){return receiver;}
    public Date getDate(){return date;}
    public void setDate(Date date){
>>>>>>> ab06e4752b3db66e082810fb4c78cca06631d21c
        this.date=date;
    }
    public Boolean equals(Message other){return other.getDate()==this.getDate();}
    public String toString(){return "Sender:"+ getSender()+" Receiver:"+ getReceiver()+
            " Date:"+ getDate()+" Message:"+ getMessage();}
}
