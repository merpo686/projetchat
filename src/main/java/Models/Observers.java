package Models;

public class Observers {
    public interface ObserverMessage {
        /**
         * Observer for the reception and sending of messages (as in conversation messages)
         * @param mess - message received
         */
        void messageHandler(Message mess);
    }
    public interface ObserverDisconnection{
        /**
         * Observer to notify the disconnection of a user
         */
        void userDisconnected(User user);
    }
    public interface ObserverConnection {
        /**
         * Observer to notify the connection of a user
         */
        void userConnected(User user);
    }
}
