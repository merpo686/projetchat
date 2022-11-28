package Managers;
import Models.*;

import java.net.SocketException;
import java.util.ArrayList;

public class ThreadManager {
    public ThreadManager(){
    }
    static public void Send_BC(Notifications notif) throws SocketException {
        new Thread(new ThreadSendBC(notif)).start();
    }
    static public void Send_BC(Validation valid) throws SocketException {
        new Thread(new ThreadSendBC(valid)).start();
    }
    static public ThreadRcvBC Start_RcvThread(handler){
        ThreadRcvBC T_Recv_BC;
        T_Recv_BC= new ThreadRcvBC(handler);
        new Thread(T_Recv_BC).start();
        return T_Recv_BC;
    }
}
