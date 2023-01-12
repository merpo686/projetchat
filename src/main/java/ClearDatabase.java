import database.ConnectionError;
import database.DatabaseManager;

public class ClearDatabase {
    /** To delete the Database. Will suppress all conversations */
    public static void main(String[] args) throws ConnectionError {
        DatabaseManager.getInstance().clearDB("messages.sqlite");
    }
}
