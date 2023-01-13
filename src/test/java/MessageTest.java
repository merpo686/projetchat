
import Models.Message;
import Models.User;
import Conversations.ConnectionError;

import Conversations.MessageAccessProblem;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class MessageTest {

    @Test
    public void MessageTest() throws ConnectionError, SQLException, UnknownHostException, MessageAccessProblem {
        String myPseudo = "Tim";
        String message = "test.sqlite";
        String hostname = InetAddress.getLocalHost().getHostName();
        String receve =" merlin   ";
        String hostrecev = "bfvuebv";
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        Message mess1 = new Message(new User(hostname,myPseudo),new User(hostrecev,receve),message);
        mess1.setDate(now);
        Message mess2 = new Message(new User(hostname,myPseudo),new User(hostrecev,receve),message);
        mess2.setDate(now);
        System.out.println(mess1+"\n");
        System.out.println(mess2+"\n");
        String Neo="bonjour";
        assert (mess1.getSender()).isEquals(mess2.getSender().getHostname());
        assert mess1.getDate().equals(mess2.getDate());
        assert mess1.getMessage().equals(mess2.getMessage());
        assert mess1.isEquals(mess2);
    }
}