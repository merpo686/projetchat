import Models.HandlerUDP;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class UDPServerTest {
    private static final Logger LOGGER = LogManager.getLogger(UDPServerTest.class);
    static boolean connected;
    static String pseudo;
    /**
     * Testing the connection process through UDP by broadcasting to myself
     */
    @Test
    public void UDPTest() {

        int testPortUDP = 5678;
        ThreadManager.portUDP = testPortUDP;
        /*
          Handler of the UDP TestServer
         */
        HandlerUDP handlerUDP = (hostname, message) -> {
            if (message.equals("true")||message.equals("false")) {
                LOGGER.debug("We received a Boolean: "+message);
                //Boolean=True: connection
                //Boolean=False: disconnection
                connected = Boolean.parseBoolean(message);
            } else {
                LOGGER.debug("We received a pseudo: "+message);
                pseudo = message;
            }
        };
        //IgnoreSelf = false; permits to not ignore our own udp messages, it is normally at true
        ThreadManager.StartUDPServer(testPortUDP,handlerUDP, false);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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