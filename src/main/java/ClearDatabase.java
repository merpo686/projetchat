import Conversations.ConnectionError;
import Conversations.ConversationsManager;

public class ClearDatabase {
    /** To delete the Database. Will suppress all conversations */
    public static void main(String[] args) throws ConnectionError {
        ConversationsManager.getInstance().clearDB("messages.sqlite");
    }
}
