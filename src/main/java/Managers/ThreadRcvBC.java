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
            String rcvData;
            DatagramSocket socket = new DatagramSocket(Self.getInstance().get_User().get_Port());
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
                    rcvNotif = new DatagramPacket(data, data.length, rcvNotif.getAddress(), rcvNotif.getPort());
                    rcvData = new String(rcvNotif.getData(), 0, rcvNotif.getLength());
                    //we need to check if it is a pseudo or a connection/disconnection... we split the string that we received with "-" as a delimiter. If we only have 1 element then that means it's a notification because it does not contain the boolean, if we have 2 elements that means it's a validation
                    String[] splitString = rcvData.split("-");
                    for (String s : splitString) {
                        System.out.println(s);
                    }
                    NotifPseudo notif;
                    Connection connect;
                    if (splitString.length == 1) {
                        notif = new NotifPseudo(new User(rcvNotif.getAddress().getHostName(),Self.getInstance().get_User().get_Port()), splitString[0]);
                        System.out.println("C'est une notification");
                        this.notifHandler.handler(notif);
                    } else if (splitString.length == 2) {
                        connect = new Connection(new User(rcvNotif.getAddress().getHostName(), Self.getInstance().get_User().get_Port()), Boolean.parseBoolean(splitString[1]));
                        System.out.println("C'est une connection");
                        this.notifHandler.handler(connect);
                    } else {
                        System.out.println("[ThreadRcvBC] Error there are 3 or more separate fields in the broadcast message");
                    }
                    notif =null;
                    connect =null;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
