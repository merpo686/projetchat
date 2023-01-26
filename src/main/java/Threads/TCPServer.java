package Threads;

import Models.HandlerTCP;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**Server class which accepts new conversations and create a receiving thread for each -
 * also adds them to the list of active conversation */
public class TCPServer extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);
    private final int portTCP;
    private final HandlerTCP handlerTCP;
    public TCPServer(int portTCP,HandlerTCP handlerTCP){
        this.portTCP = portTCP;
        this.handlerTCP = handlerTCP;
    }

    public void run(){
        //creation du server socket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(portTCP);
        } catch (IOException e) {
            LOGGER.error("Failed to create a new ServerSocket on port "+portTCP);
            e.printStackTrace();
        }
        //Server is running full time while the app is active if there are no exceptions
        while(true) {
            LOGGER.debug("Waiting for TCP connection");
            Socket link = null;
            try {
                if (socket != null) {
                    link = socket.accept();
                }
            } catch (IOException e) {
                LOGGER.error("Socket accept() returned an error.");
                e.printStackTrace();
            }
            LOGGER.debug("A new connection identified at : " + link);
            handlerTCP.handle(link);
        }
    }
}
