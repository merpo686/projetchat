import ActivityManagers.Self;
import Threads.UDPServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.net.*;

import static Threads.ThreadManager.SendUDPBC;
import static java.lang.Thread.sleep;

public class UDPTest {
    private static final Logger LOGGER = LogManager.getLogger(UDPTest.class);
    private int testPort = 5678;

    @Test
    public void UDPTest() throws InterruptedException {
        Self self = Self.getInstance();
        self.setPseudo("Tim");
        UDPServer udpServer = new UDPServer(testPort, false);
        udpServer.setDaemon(true);
        udpServer.start();
        SendUDPBC("true", testPort);    //Sends a connection in broadcast (but really to myself since we are testing here)
        sleep(2); //waiting for udp server to send back the pseudo 'Tim' to myself
        self.setPseudo("Gerard"); //choosing my pseudo
        SendUDPBC(Self.getInstance().getPseudo(), testPort); //sending my pseudo in broadcast to all active users
        LOGGER.debug("Broadcast pseudo to all active users");
    }
}