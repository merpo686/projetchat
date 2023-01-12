import Managers.Self;
import Models.Message;
import Models.User;
import database.ConnectionError;
import database.DatabaseManager;
import database.MessageAccessProblem;
import org.junit.Test;

import java.net.UnknownHostException;
import java.sql.SQLException;

public class DatabaseTest {

    @Test
    public void DBTest() throws ConnectionError, SQLException, UnknownHostException, MessageAccessProblem {
        String myPseudo = "Tim";
        Self.getInstance().setPseudo(myPseudo);
        String TestDBName = "test.sqlite";
        DatabaseManager myDB = DatabaseManager.getInstance();
        User testUser = new User("testHost", "Patrick");
        Message mess = new Message(new User(Self.getInstance().getHostname(), Self.getInstance().getPseudo()), testUser , "Bonjour Test Test");

        //testing connection
        myDB.clearDB(TestDBName);
        myDB.connectDB(TestDBName);

        //testing conversation methods
        myDB.createTableConversations();
        assert myDB.checkExistTableConversations();
        System.out.println("Conversations table created");
        myDB.addConversation(testUser.getHostname());
        assert myDB.checkExistConversation(testUser.getHostname());
        System.out.println("Conversation added in conversations table");

        //testing message methods
        myDB.addMessage(mess);
        myDB.createTableMessages(testUser.getHostname());
        assert myDB.checkExistTableMessages();
        System.out.println("Messages table created");
        myDB.addMessage(mess);
        System.out.println("Message added for the conversation with "+testUser.getHostname());
        myDB.getAllMessages(testUser.getHostname());
        myDB.getLastMessage(testUser.getHostname());
        myDB.showMessages(testUser.getHostname());
        myDB.showTables();
    }
}