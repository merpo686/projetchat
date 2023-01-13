package Threads;

import Models.*;
import Conversations.ConnectionError;
import Conversations.ConversationsManager;
import Models.ObserverReception;
import ActivityManagers.Self;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;

public class TCPClientHandler extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(TCPClientHandler.class);
    private final Socket socket;
    private final User dest;
    DataInputStream inputStream;

    private ObserverReception observer;

    public void attach(ObserverReception observer){
        this.observer= observer;
    }

    public void notifyObserver(Message mess){
        observer.update(mess);
    }
    /**
     * Constructor
     * @param link
     * @param dest
     */
    public TCPClientHandler(Socket link,User dest){
        this.setDaemon(true);
        this.socket = link;
        this.dest=dest;
        this.attach(ConversationsManager.getInstance());
    }
    /**returns the socket of the conversation
     * @return socket
     * */
    public Socket getSocket() {
        return socket;
    }
 /** to be sure to stop the thread when calling interrupt */
    @Override
    public void interrupt() {
        super.interrupt();
        try {
            inputStream.close(); //Closing the stream if interrupt didn't work
        } catch (IOException e) {
            LOGGER.error("Failed closing the inputstream when stopping the thread.");
            e.printStackTrace();
        }
    }

    /** Run method of thread*/
    public void run(){
        try{
            //we recover the input stream
            inputStream = new DataInputStream(socket.getInputStream());
            while(true) {
                //getting message
                String received = inputStream.readUTF();
                LOGGER.debug("Received message TCP: "+received);
                Message mess = new Message(dest,new User(Self.getInstance().getHostname(),Self.getInstance().getPseudo()), received);
                ConversationsManager.getInstance().addMessage(mess);
                notifyObserver(mess);
            }
        } catch (InterruptedIOException e) { //If interrupted
            try {
                socket.close();
            } catch (IOException ioException) {
                LOGGER.error("Failed closing socket when interrupting the thread.");
                ioException.printStackTrace();
            }
            Thread.currentThread().interrupt();
            LOGGER.debug("Interrupted with InterruptedIOException.");
        } catch (IOException e) {
            if (!isInterrupted()) {
                LOGGER.error("Exception other than interruption while running.");
                e.printStackTrace();
            } else { //interrupted but InterruptedIOException wasn't triggered
                try {
                    socket.close();
                } catch (IOException ioException) {
                    LOGGER.error("Failed closing socket when interrupting the thread.");
                    ioException.printStackTrace();
                }
                LOGGER.debug("Interrupted.");
            }
        } catch (SQLException | ConnectionError throwables) {
            LOGGER.error("Error due to database connection or request.");
            throwables.printStackTrace();
        }
    }
}
