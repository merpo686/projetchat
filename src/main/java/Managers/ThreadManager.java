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
    static public void Send_BC(Validation valid) throws SocketException {
        new Thread(new ThreadSendBC(valid)).start();
    }
    static public ThreadRcvBC Start_RcvThread(UserMain.NotifHandler handler){
        ThreadRcvBC T_Recv_BC;
        T_Recv_BC= new ThreadRcvBC(handler);
        new Thread(T_Recv_BC).start();
        return T_Recv_BC;
    }
}
