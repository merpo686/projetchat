package Managers;

import Models.*;
import com.sun.nio.sctp.NotificationHandler;

import java.lang.*;
import java.io.IOException;
import java.net.*;

public class ThreadRcvBC extends Thread {
    private final ThreadManager.NotifHandler notifHandler;
    public ThreadRcvBC(ThreadManager.NotifHandler handler) {
        this.notifHandler=handler;
    }

    public void run() {
        try {
            byte[] data = new byte[1024];
            DatagramSocket socket = new DatagramSocket(Self.getInstance().get_User().getPortUDP());
            DatagramPacket rcvNotif = new DatagramPacket(data, data.length);
            //we receive the model (either notification or validation)
            while (true) { //the thread will be in a receiving state constantly
                try {
                    System.out.println("[ThreadRcvBC] Waiting for Broadcast");
                    socket.receive(rcvNotif);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (rcvNotif.getLength() == 0) {
                    System.out.println("[ThreadRcvBC] Read zero bytes");
                } else if (!(rcvNotif.getAddress().getHostName().contains(Self.getInstance().get_User().get_Hostname()))){ //on ne reçoit pas de nous mêmes
                    String rcvData = new String(rcvNotif.getData(), 0, rcvNotif.getLength());
                    //we need to check if it is a pseudo or a connection/disconnection... we split the string that we received with "-" as a delimiter. If we only have 1 element then that means it's a notification because it does not contain the boolean, if we have 2 elements that means it's a validation
                    if (rcvData.equals("true")||rcvData.equals("false")) {
                        Connection connect;
                        connect = new Connection(new User(rcvNotif.getAddress().getHostName(),-1, Self.getInstance().get_User().getPortUDP()), Boolean.parseBoolean(rcvData));
                        System.out.println("C'est une connection/deconnection "+rcvData);
                        this.notifHandler.handler(connect);
                    } else {
                        NotifPseudo notif;
                        notif = new NotifPseudo(new User(rcvNotif.getAddress().getHostName(),-1,Self.getInstance().get_User().getPortUDP()), rcvData);
                        System.out.println("C'est un pseudo "+rcvData);
                        this.notifHandler.handler(notif);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
