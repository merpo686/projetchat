import ActivityManagers.Self;
import Graphics.Interface;
import Models.Message;
import Models.Observers;
import Models.User;
import Threads.TCPClientHandler;
import Threads.TCPServer;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TCPTest {
    private static final Logger LOGGER = LogManager.getLogger(UDPTest.class);
    private int testPort = 12342;

    @Test
    public void TCPConnectionTest() {
       /* Self self = Self.getInstance();
        User user =new User(self.getHostname(),self.getPseudo());

        TCPServer tcpServer= new TCPServer(testPort);
        tcpServer.setDaemon(true);
        tcpServer.start();

        assert (ThreadManager.getInstance().getActiveconversation(user))!=null;*/
    }
}
