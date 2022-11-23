package Managers;
import Models.*;

import java.util.ArrayList;

public class ThreadManager {
    ThreadRcvBC T_Recv_BC;
    ThreadSendBC T_Send_BC;
    public ThreadManager(){
        Recv_BC= new ThreadRcvBC();
        Send_BC= new ThreadSendBC();
    }
    static public void Send_BC(Notif notif){
        T_Send_BC.send(notif);
    }
}
