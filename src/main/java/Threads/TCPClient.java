package Threads;

import Models.*;
import ActivityManagers.Self;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.net.Socket;


public class TCPClient extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(TCPClient.class);
    private final Socket socket;
    private final User dest;
    DataInputStream inputStream;
    public static HandlerMessageReceived handlerMessageReceived;
    /**
     * Constructor
     * @param link TCP socket opened
     * @param dest User with whom the thread is opened
     */
    public TCPClient(Socket link, User dest){
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
                LOGGER.info("Received message: "+received);
                Message mess = new Message(dest,new User(Self.getInstance().getHostname(),Self.getInstance().getPseudo()), received);
                handlerMessageReceived.handle(mess);
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
            try {
                socket.close();
            } catch (IOException ioException) {
                LOGGER.error("Failed closing socket when interrupting the thread.");
                ioException.printStackTrace();
            }
            LOGGER.debug("Interrupted.");
        }
    }
}
