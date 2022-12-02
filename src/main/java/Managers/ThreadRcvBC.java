package Managers;

import Models.*;
import java.lang.*;
import java.io.IOException;
import java.net.*;

public class ThreadRcvBC implements Runnable {
    private Validation valid;
    private NotifPseudo notif;

    public ThreadRcvBC(UserMain.NotifHandler handler) {
    }

    public void run() {
        try {
            byte[] data = new byte[1024];
            String rcvData;
            DatagramSocket socket = new DatagramSocket(UserMain.getInstance().Get_User().get_Port());
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
                } else if (!(rcvNotif.getAddress().getHostName().contains(UserMain.getInstance().Get_User().get_Hostname()))){
                    rcvNotif = new DatagramPacket(data, data.length, rcvNotif.getAddress(), rcvNotif.getPort());
                    rcvData = new String(rcvNotif.getData(), 0, rcvNotif.getLength());
                    //we need to check if notification or validation... we split the string that we received with "-" as a delimiter. If we only have 1 element then that means it's a notification because it does not contain the boolean, if we have 2 elements that means it's a validation
                    String[] splitString = rcvData.split("-");
                    for (int i=0; i< splitString.length; i++){
                        System.out.println(splitString[i]);
                    }
                    if (splitString.length == 1) {
                        this.notif = new NotifPseudo(new User(rcvNotif.getAddress().getHostName(),UserMain.getInstance().Get_User().get_Port()), splitString[0]);
                        System.out.println("C'est une notification");
                        NetworkManager.Process_Notif_Pseudo(this.notif);
                    } else if (splitString.length == 2) {
                        this.valid = new Validation(new User(rcvNotif.getAddress().getHostName(), UserMain.getInstance().Get_User().get_Port()), splitString[0], Boolean.parseBoolean(splitString[1]));
                        System.out.println("C'est une validation");
                        NetworkManager.Process_Validation(this.valid);
                    } else {
                        System.out.println("[ThreadRcvBC] Error there are 3 or more separate fields in the broadcast message");
                    }
                    this.notif=null;
                    this.valid=null;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
