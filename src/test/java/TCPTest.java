import ActivityManagers.Self;
import Models.*;
import Threads.TCPClient;
import Threads.ThreadManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class testing the TCP connection and the sending/receiving of a message:
 * -the server is started
 * -client connects to server
 * -server sends a message to client
 * -client sends a message to server
 */
public class TCPTest {
    private static final Logger LOGGER = LogManager.getLogger(UDPServerTest.class);
    static boolean UserConnected;
    static boolean messageReceived;
    static Socket socketServer;
    @Test
    public void TCPConnectionTest() {
        Configurator.setRootLevel(Level.INFO);
        Self self = Self.getInstance();
        self.setPseudo("Mario&Luigi at the Winter Olympics");
        User user = new User(self.getHostname(), self.getPseudo()); //here the send and receiver are the same sine we have only one hostname
        Message mess = new Message(user, user, "This message is a test of TCP connection.");
        Message mess2 = new Message(user,user,"Second message");
        UserConnected=false;
        messageReceived =false;

        /*Handler of the TCPServer:
          - proves the connection occurred;
          - provides the socket, with which we will send a message.
         */
        HandlerTCP handlerTCP = link -> {
            UserConnected = true;
            LOGGER.info("Connection received");
            socketServer = link;
            TCPClient thread = new TCPClient(link, user); //creating the conversation receiving thread
            thread.start();
        };

        /*Handler of TCPClient:
          - proves we received a message;
          - prints it.
         */
        TCPClient.handlerMessageReceived = mess1 -> {
            messageReceived = true;
            LOGGER.info("Message well received. Message: "+mess1.getMessage());
        };

        //starts TCP server on test port 1
        //sender
        int testPort1 = 12342;
        ThreadManager.StartTCPServer(testPort1, handlerTCP);
        LOGGER.debug("TCPServer started.");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //getting socket to connect to ServerSocket
        Socket socket = null;
        try {
            socket = new Socket(user.getHostname(), testPort1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (UserConnected); //should be true if socket connected well

        TCPClient tcpClientHandler = new TCPClient(socket, user); //will receive and print the message
        tcpClientHandler.start();

        //sending message from server to client
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(socketServer.getOutputStream());
            outputStream.writeUTF(mess.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Unable to send the message via TCP.");
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (messageReceived);

        messageReceived=false;
        //sending message from client to server
        DataOutputStream outputStream2;
        try {
            outputStream2 = new DataOutputStream(socket.getOutputStream());
            outputStream2.writeUTF(mess2.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Unable to send the message via TCP.");
            e.printStackTrace();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert (messageReceived);
    }
}
