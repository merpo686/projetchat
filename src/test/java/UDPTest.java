import ActivityManagers.ActiveUserManager;
import ActivityManagers.Self;
import Models.HandlerUDP;
import Models.User;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;;

public class UDPTest {
    private static final Logger LOGGER = LogManager.getLogger(UDPTest.class);
    private final int testPortUDP = 5678;
    static boolean connected = false;
    static String pseudo;
    /**
     * Testing the connection process through UDP by broadcasting to myself
     */
    @Test
    public void UDPTest() {

        ThreadManager.portUDP = testPortUDP;
        HandlerUDP handlerUDP = new HandlerUDP() {
            @Override
            public void handle(String hostname, String message) {
                if (message.equals("true")||message.equals("false")) {
                    LOGGER.debug("We received a Boolean: "+message);
                    if (Boolean.parseBoolean(message)){ //Boolean=True: connection
                        connected = true;
                    }
                    else { //Boolean=False: disconnection
                        connected = false;
                    }
                } else {
                    LOGGER.debug("We received a pseudo: "+message);
                    pseudo = message;
                }
            }
        };
        //IgnoreSelf = false permits to not ignore our own udp messages, it is normally at true
        ThreadManager.StartUDPServer(testPortUDP,handlerUDP, false);
        ThreadManager.SendConnection();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (connected);

        ThreadManager.SendPseudo("Test");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (pseudo.equals("Test"));

        ThreadManager.SendDisconnection();
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (!connected);
    }
}