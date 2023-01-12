package Managers;

import Models.*;
import database.ConnectionError;
import database.ConversationsTableDoesNotExist;
import database.DatabaseManager;
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

    /**
     * Constructor
     * @param link
     * @param dest
     */
    public TCPClientHandler(Socket link,User dest){
        this.setDaemon(true);
        this.socket = link;
        this.dest=dest;
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
                Message mess = new Message(new User(Self.getInstance().getHostname(),Self.getInstance().getPseudo()), dest, received);
                if (!DatabaseManager.getInstance().checkExistConversation(dest.getHostname())) {
                    DatabaseManager.getInstance().addConversation(dest.getHostname());
                }
                DatabaseManager.getInstance().addMessage(mess);
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
        } catch (ConversationsTableDoesNotExist conversationsTableDoesNotExist) {
            conversationsTableDoesNotExist.printStackTrace();
        }
    }
}
