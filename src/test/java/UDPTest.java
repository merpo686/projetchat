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

    /**
     * Testing the connection process through UDP by broadcasting to myself
     * @throws InterruptedException
     */
    @Test
    public void UDPTest() throws InterruptedException {
        Self self = Self.getInstance();
        self.setPseudo("Tim"); //simulated user's pseudo

        //1. Start the UDP Server which handles the reception side of UDP
        /*UDPServer udpServer = new UDPServer(testPort, false);
        udpServer.setDaemon(true);
        udpServer.start();

        //2. Send a broadcast UDP with true as a parameter to represent that we want to connect to all active users
        SendUDPBC("true", testPort);

        //3. A user that is already connected to the session receives the broadcast connection with UDP Server and sends back his pseudo  (here it's 'Tim')
        //...the function that does this is called in UDPServer
        sleep(2); //waiting for udp server to send back the pseudo 'Tim' to myself
        //4. The user who wants to connect receives the pseudo 'Tim'. He stocks it in the active user list,
        // and has to choose a pseudo himself, here we are choosing 'Gerard' (note : it cannot be 'Tim' since it is already taken)
        self.setPseudo("Gerard");
        //5. He then sends his chosen pseudo in broadcast to all active users
        SendUDPBC(self.getPseudo(), testPort);
        LOGGER.debug("Broadcast my pseudo to all active users");*/
    }
}