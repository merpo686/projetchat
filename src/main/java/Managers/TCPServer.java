package Managers;

import Models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/**Server class which accepts new conversations and create a receiving thread for each -
 * also adds them to the list of active conversation */
public class TCPServer extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(TCPServer.class);
    public TCPServer() {}
    public void run(){
        //creation du server socket
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(Self.portTCP);
        } catch (IOException e) {
            LOGGER.error("Failed to create a new ServerSocket on port "+Self.portTCP);
            e.printStackTrace();
        }
        //Server is running full time while the app is active if there is no exceptions
        while(true) {
            LOGGER.debug("Waiting for TCP connection");
            Socket link = null;
            try {
                link = socket.accept();
            } catch (IOException e) {
                LOGGER.error("Socket accept() returned an error.");
                e.printStackTrace();
            }
            LOGGER.debug("A new connection identified at : " + link);
            User dest = ActiveUserManager.getInstance().get_User(link.getInetAddress().getHostName());
            TCPClientHandler thread = new TCPClientHandler(link,dest); //creating the conversation receiving thread
            thread.start();
            ThreadManager.getInstance().addActiveconversation(dest,thread); //adding the thread to the list of active conversations
        }
    }
}
