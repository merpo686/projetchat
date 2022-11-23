package Managers;
import Models.*;

import java.net.SocketException;
import java.util.ArrayList;

public class ThreadManager {
    ThreadRcvBC T_Recv_BC;
    NetworkManager NM;
    public ThreadManager(NetworkManager NM){
        this.NM=NM;
        this.T_Recv_BC= new ThreadRcvBC(NM);
        System.out.println("Construction de TM");
        new Thread(this.T_Recv_BC).start();
    }
    static public void Send_BC(Notifications notif) throws SocketException {
        new Thread(new ThreadSendBC(notif)).start();
    }
    static public void Send_BC(Validation valid) throws SocketException{
        new Thread(new ThreadSendBC(valid)).start();
    }
}
