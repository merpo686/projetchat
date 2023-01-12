import database.ConnectionError;
import database.DatabaseManager;

public class ClearDatabase {
    public static void main(String[] args) throws ConnectionError {
        DatabaseManager.getInstance().clearDB("messages.sqlite");
    }
}
