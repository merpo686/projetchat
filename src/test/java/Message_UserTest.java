
import Models.Message;
import Models.User;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

/**
 * Tests the implementation of Message and User
 */
public class Message_UserTest {

    @Test
    public void MessageUserTest() throws UnknownHostException {
        String pseudo1 = "Tim";
        String message1 = "test.sqlite";
        String hostname1 = InetAddress.getLocalHost().getHostName();
        String pseudo2 ="merlin";
        String hostname2 = "bfvuebv";
        String message2 = "fbsiugq";
        LocalDateTime now1 = LocalDateTime.now();
        LocalDateTime now2 = LocalDateTime.now();
        Message mess1 = new Message(new User(hostname1, pseudo1),new User(hostname2, pseudo2),message1);
        mess1.setDate(now1);
        Message mess2 = new Message(new User(hostname2, pseudo2),new User(hostname1, pseudo1),message2);
        mess2.setDate(now2);
        //two same user
        assert (new User(hostname1,pseudo1).equals(hostname1 ));
        //two different users
        assert !(new User(hostname1,pseudo1).equals(hostname2 ));
        //two different messages
        assert !( mess1.equals(mess2));
    }
}