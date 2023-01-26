import ActivityManagers.Self;
import Graphics.Interface;
import Models.*;
import Threads.TCPClientHandler;
import Threads.TCPServer;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPTest {
    private static final Logger LOGGER = LogManager.getLogger(UDPTest.class);
    private int testPort = 12342;
    static boolean UserConnected = false;
    static boolean messageReceived = false;

    @Test
    public void TCPConnectionTest() {
        Self self = Self.getInstance();
        self.SetTCPPort(testPort);
        self.SetUDPPort(5732);
        self.setPseudo("Mario&Luigi at the Winter Olympics");
        User user =new User(self.getHostname(),self.getPseudo());
        Message mess = new Message(user, user, "We will win YIHAH!");

        HandlerTCP handlerTCP = new HandlerTCP() {
            @Override
            public void handle(Socket link) {
                UserConnected =true;
            }
        };

        HandlerMessageReceived handlerMessageReceived = new HandlerMessageReceived() {
            @Override
            public void handle(Message mess) {
                //blbla message bien recu
                messageReceived = true;
            }
        };
        TCPClientHandler.handlerMessageReceived =handlerMessageReceived;
        //getting socket
        User dest =mess.getReceiver();
        TCPClientHandler tcpClientHandler  = ThreadManager.getInstance().getActiveconversation(dest);
        Socket socket = null;
        if (tcpClientHandler==null) {
            try {
                socket = new Socket(dest.getHostname(), Self.getInstance().portTCP);
            } catch (IOException e) {
                LOGGER.debug("Unable to create TCP socket. Hostname: " + dest.getHostname() + " Port TCP: " + Self.getInstance().portTCP);
                e.printStackTrace();
                LOGGER.info("Please return to choose discussion window, user certainly disconnected.");
            }
            tcpClientHandler = new TCPClientHandler(socket, dest);
            tcpClientHandler.start();
            ThreadManager.getInstance().addActiveconversation(dest, tcpClientHandler);
        }
        socket = tcpClientHandler.getSocket();


        TCPServer tcpServer= new TCPServer(testPort,handlerTCP);
        tcpServer.setDaemon(true);
        tcpServer.start();
        assert(UserConnected);

        //sending message
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(mess.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Unable to send the message via TCP.");
            e.printStackTrace();
        }
        assert(messageReceived);


        /*
        //tester ici la reception/envoi de message
        TCPClientHandler tcpClientHandler = new TCPClientHandler(socket, user);
        tcpClientHandler.start();
        socket = tcpClientHandler.getSocket();
        DataOutputStream outputStream;
        try{
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(mess.getMessage());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        */
    }
}
