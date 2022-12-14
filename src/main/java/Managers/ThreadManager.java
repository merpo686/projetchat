package Managers;
import Models.*;
import com.sun.nio.sctp.NotificationHandler;

import java.net.SocketException;
import java.util.ArrayList;

public class ThreadManager {
    public ThreadManager(){
    }
    static public void Send_BC(NotifPseudo notif) throws SocketException {
        new Thread(new ThreadSendBC(notif)).start();
    }
    static public void Send_BC(Connection connect) throws SocketException {
        new Thread(new ThreadSendBC(connect)).start();
    }
    static public ThreadRcvBC Start_RcvThread(UserMain.NotifHandler handler){
        ThreadRcvBC T_Recv_BC;
        T_Recv_BC= new ThreadRcvBC(handler);
        T_Recv_BC.setDaemon(true);
        T_Recv_BC.start();
        return T_Recv_BC;
    }
    static public void Send_Pseudo_Unicast(NotifPseudo notifPseudo) throws SocketException {
        new Thread(new Thread_Send_Pseudo_Unicast(notifPseudo)).start();
    }
}
