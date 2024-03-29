package Models;
import java.time.*;
public class Message {
    private LocalDateTime date;
    private final User sender;
    private final User receiver;
    private final String data;
    /**
     * Constructor
     * @param sender user who sends the message
     * @param receiver user who received it
     * @param messageData content of the message
     */
    public Message(User sender, User receiver, String messageData) {
        date = LocalDateTime.now();
        this.sender=sender;
        this.receiver=receiver;
        this.data=messageData;
    }
    /**
     * @return the content of the message
     */
    public String getMessage(){return data;}
    /**
     * @return sender user
     */
    public User getSender(){return sender;}
    /**
     * @return receiver user
     */
    public User getReceiver(){return receiver;}
    /**
     * @return date
     */
    public LocalDateTime getDate(){return date;}
    /**
     *To modify the date of a message (only used when retrieving a message from database, to set the date
     * @param date date
     */
    public void setDate(LocalDateTime date){
        this.date=date;
    }
    /**
     * Compares two messages
     * @param other - Message to compare
     * @return true if same message
     */
    public Boolean equals(Message other){return (other.getDate()).equals(this.date) &&
            (other.getMessage()).equals(this.getMessage()) &&
            (other.getReceiver()).equals(this.getReceiver().getHostname()) &&
            (other.getSender()).equals(this.getSender().getHostname());}
    /**
     * @return String value of Message
     */
    public String toString(){return "Sender:"+ getSender()+" Receiver:"+ getReceiver()+
            " Date:"+ getDate()+" Message:"+ getMessage();}
}
