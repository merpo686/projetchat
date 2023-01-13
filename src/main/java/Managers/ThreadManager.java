package Managers;
import Models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.net.SocketException;
import java.util.*;

/**This class contains all the managing-thread related functions */
public class ThreadManager {

    private static final Logger LOGGER = LogManager.getLogger(ThreadManager.class);
    private final Map<User,TCPClientHandler> map_active_conversations; //list of active conversations (active TCP threads)
    static ThreadManager instance;
    private final UDPServer udpServer;

    /**
     * Constructor
     */
    private ThreadManager() {
        map_active_conversations = new HashMap<>();
        udpServer = new UDPServer();
        udpServer.setDaemon(true);
        udpServer.start();
    }

    /**
     * @return the instance of ThreadManager
     */
    public static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }

    /**
     * @return the active ThreadRecvUDP
     */
    public UDPServer getUdpServer(){return udpServer;}
    /**
     * Add a thread to the list of active conversation threads
     * @param dest
     * @param thread
     */
    public void addActiveconversation(User dest, TCPClientHandler thread){map_active_conversations.put(dest,thread);}

    /**
     * Delete a thread to the list of active conversation threads
     * @param dest
     */
    public void delActiveconversation(User dest){
        TCPClientHandler thread = map_active_conversations.remove(dest);
        if (thread!=null){
            thread.interrupt();
        }
    }

    /**
     * @param dest
     * @return the conversation thread corresponding to the user, null if not exist
     */
    public TCPClientHandler getActiveconversation(User dest){return map_active_conversations.get(dest);}

    /** Closes all threads, active conversation and recv servers*/
    public void deleteAllThreads(){
        //close active conversations threads
        for (User dest : map_active_conversations.keySet()){
            delActiveconversation(dest);
        }
    }
    /**Start TCP server for accepting new conversations*/
    static public void StartTCPServer() {
        TCPServer tcpServer= new TCPServer();
        tcpServer.setDaemon(true);
        tcpServer.start();
    }
}
