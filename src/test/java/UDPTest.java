import ActivityManagers.ActiveUserManager;
import ActivityManagers.Self;
import Models.HandlerUDP;
import Models.User;
import Threads.ThreadManager;
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
    private int testPortUDP = 5678;
    static boolean firstConnection = false;
    static boolean connected = false;

    /**
     * Testing the connection process through UDP by broadcasting to myself
     * @throws InterruptedException
     */
    @Test
    public void UDPTest() throws InterruptedException {
        Self self = Self.getInstance();
        self.SetUDPPort(testPortUDP);
        self.setPseudo("Tim"); //simulated user's pseudo
        ThreadManager threadManager = ThreadManager.getInstance();

        HandlerUDP handlerUDP = new HandlerUDP() {
            @Override
            public void handle(String hostname, String message) {
                if (message.equals("true")||message.equals("false")) {
                    LOGGER.debug("We received a connection/disconnection notification "+message);
                    if (Boolean.parseBoolean(message) && Self.getInstance().getPseudo()!=null){
                        ThreadManager.SendPseudoUnicast(hostname, testPortUDP); //we received a connection notification, we respond our pseudo if we chose it
                        firstConnection = true;
                    }
                    else {
                        User user = ActiveUserManager.getInstance().removeListActiveUser(hostname); //we remove the user from the list of active user
                        if (user == null){
                            LOGGER.debug("we didn't knew this user was connected. His disconnection is thus irrelevant");
                        }
                        else{
                            threadManager.notifyDisconnection(user);
                            connected = false;
                        }
                    }
                } else {
                    User user = new User(hostname, message);
                    LOGGER.debug("We received a username/pseudo "+message);
                    ActiveUserManager.getInstance().changeListActiveUser(user);
                    connected = true;
                }
            }
        };
        ThreadManager.StartUDPServer(testPortUDP,handlerUDP, false);
        assert(firstConnection);
        assert(connected);

        /*
        //1. Start the UDP Server which handles the reception side of UDP
        UDPServer udpServer = new UDPServer(testPortUDP, false, handlerUDP);
        udpServer.setDaemon(true);
        udpServer.start();

        //2. Send a broadcast UDP with true as a parameter to represent that we want to connect to all active users
        SendUDPBC("true", testPortUDP);

        //3. A user that is already connected to the session receives the broadcast connection with UDP Server and sends back his pseudo  (here it's 'Tim')
        //...the function that does this is called in UDPServer
        sleep(2); //waiting for udp server to send back the pseudo 'Tim' to myself
        //4. The user who wants to connect receives the pseudo 'Tim'. He stocks it in the active user list,
        // and has to choose a pseudo himself, here we are choosing 'Gerard' (note : it cannot be 'Tim' since it is already taken)
        self.setPseudo("Gerard");
        //5. He then sends his chosen pseudo in broadcast to all active users
        SendUDPBC(self.getPseudo(), testPort);
        LOGGER.debug("Broadcast my pseudo to all active users");
        */
    }
}