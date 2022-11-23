package Managers;
import Models.*;

import java.util.ArrayList;

public class ThreadManager {
    ThreadRcvBC T_Recv_BC;
    ThreadSendBC T_Send_BC;
    UserManager UM;
    public ThreadManager(UserManager UM){
        this.UM=UM;
        Recv_BC= new ThreadRcvBC(UM);
        Send_BC= new ThreadSendBC();
    }
    static public void Send_BC(Notif notif){
        T_Send_BC.send(notif);
    }
}
