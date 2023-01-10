import Managers.Self;
import Models.Message;
import Models.User;
import database.ConnectionError;
import database.DatabaseManager;
import database.MessageAccessProblem;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

public class DatabaseTest {

    @Test
    public void DBTest() throws ConnectionError, SQLException, UnknownHostException, SocketException, MessageAccessProblem {
        String myPseudo = "Tim";
        Self.getInstance().set_Pseudo(myPseudo);
        String TestDBName = "test.sqlite";
        DatabaseManager myDB = DatabaseManager.getInstance();
        User testUser = new User("testHost", "Patrick");
        Message mess = new Message(new User(Self.getInstance().getHostname(), Self.getInstance().get_Pseudo()), testUser , "Bonjour Test Test");

        //testing connection
        myDB.clearDB(TestDBName);
        myDB.connectDB(TestDBName);

        //testing conversation methods
        myDB.createTableConversations();
        System.out.println("Conversations Table created");
        myDB.addConversation(testUser.get_Hostname());
        System.out.println("Conversation added in conversations table");
        assert myDB.checkExistTableConversations();
        assert myDB.checkExistConversation(testUser.get_Hostname());

        //testing message methods
        myDB.createTableMessages(testUser.get_Hostname()); //faudra creer une table en vrai aussi on l'a pas encore fait (genre lorsque tu cree une connexion tcp
        myDB.addMessage(mess, testUser.get_Hostname());
        System.out.println("Message added for the conversation with "+testUser.get_Hostname());
        myDB.getAllMessages(testUser.get_Hostname());
        myDB.getLastMessage(testUser.get_Hostname());
    }
}
