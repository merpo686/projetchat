package Models;

public class Observers {
    public interface ObserverReception {
        /**
         * Observer for the reception of messages (as in conversation messages)
         * @param mess - messsage received
         */
        void messageReceived(Message mess);
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
