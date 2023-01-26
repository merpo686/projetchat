import ActivityManagers.Self;
import Models.Message;
import Models.User;
import Conversations.ConnectionError;
import Conversations.ConversationsManager;
import Conversations.MessageAccessProblem;
import org.junit.Test;
import java.sql.SQLException;


/**
 * Tests the implementation of the Database
 */
public class DatabaseTest {

    @Test
    public void DBTest() throws ConnectionError, SQLException, MessageAccessProblem {
        String myPseudo = "Tim";
        Self.getInstance().setPseudo(myPseudo);
        String TestDBName = "test.sqlite";
        ConversationsManager myDB = ConversationsManager.getInstance();
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
        Message lastMessage = myDB.getLastMessage(testUser.getHostname());
        System.out.println(lastMessage);
    }
}