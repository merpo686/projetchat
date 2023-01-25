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
    static Boolean UserConnected = false;

    @Test
    public void TCPConnectionTest() {
        Self self = Self.getInstance();
        self.SetTCPPort(testPort);
        self.setPseudo("Mario&Luigi at the Winter Olympics");
        User user =new User(self.getHostname(),self.getPseudo());

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
            }
        };
        TCPClientHandler.handlerMessageReceived =handlerMessageReceived;

        TCPServer tcpServer= new TCPServer(testPort,handlerTCP);
        tcpServer.setDaemon(true);
        tcpServer.start();
        Socket socket = null;
        try {
            socket = new Socket(self.getHostname(), self.portTCP);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert(UserConnected);

        //tester ici la reception/envoi de message
        tcpClientHandler = new TCPClientHandler(socket, dest);
        tcpClientHandler.start();
        socket = tcpClientHandler.getSocket();
        DataOutputStream outputStream;
        outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF(mess.getMessage());
    }
}
