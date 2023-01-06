package Managers;
import Models.*;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;


public class ThreadManager {
    private final Map<User,TCPClientHandler> map_active_conversations;
    static ThreadManager instance;

    private ThreadManager() {
        map_active_conversations = new HashMap<>();
    }

    public static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }
    public void add_active_conversation(User dest, TCPClientHandler thread){map_active_conversations.put(dest,thread);}
    public void del_active_conversation(User dest){
        //fermer le thread
        TCPClientHandler thread = map_active_conversations.remove(dest);
        thread.interrupt();
    }
    public TCPClientHandler get_active_conversation(User dest){return map_active_conversations.get(dest);}

    public interface NotifHandler {
        void handler(Object notif) throws SocketException, UnknownHostException;
    }

    static public void Send_BC(User user) throws SocketException {
        new Thread(new ThreadSendBC(user)).start();
    }
    static public void Send_BC(Connection connect) throws SocketException {
        new Thread(new ThreadSendBC(connect)).start();
    }
    static public void Start_RcvThread(NotifHandler handler){
        ThreadRcvBC T_Recv_BC;
        T_Recv_BC= new ThreadRcvBC(handler);
        T_Recv_BC.setDaemon(true);
        T_Recv_BC.start();
    }
    static public void Send_Pseudo_Unicast(String hostname) throws SocketException, UnknownHostException {
        new Thread(new Thread_Send_Pseudo_Unicast(hostname)).start();
    }
    static public void Start_TCP_Server() throws IOException {
        TCPServer server = new TCPServer();
    }
}
