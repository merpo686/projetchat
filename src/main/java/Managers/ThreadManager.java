package Managers;
import Models.*;

import java.util.ArrayList;

public class ThreadManager {
    ThreadRcvBC T_Recv_BC;
    ThreadSendBC T_Send_BC;
    NetworkManager NM;
    public ThreadManager(NetworkManager NM){
        this.NM=NM;
        this.T_Recv_BC= new ThreadRcvBC(NM);
        this.T_Send_BC= new ThreadSendBC();
    }
    static public void Send_BC(Notifications notif){
        T_Send_BC.send(notif);
    }

}
