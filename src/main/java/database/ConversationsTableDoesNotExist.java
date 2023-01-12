package database;

public class ConversationsTableDoesNotExist extends Exception{
    public ConversationsTableDoesNotExist() {
    }

    @Override
    public String toString() {
        return "ConversationsTableDoesNotExist{The conversations table does not exist}";
    }
}
