package Models;

/**
 * Observer interfaces, mainly to notify reception of messages to other classes
 */
public interface ObserverReception {
    /**
     * Observer for the reception of messages (as in conversation messages)
     * @param mess
     */
    void update(Message mess);

}