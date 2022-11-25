package Managers;

import Models.*;
import java.lang.*;
import java.io.IOException;
import java.net.*;

public class ThreadRcvBC implements Runnable {
    private NetworkManager NM;
    private Validation valid;
    private Notifications notif;
    public ThreadRcvBC(NetworkManager NM) {
        this.NM = NM;
    }

    public void run() {
        try {
            byte[] data = new byte[1024];
            String rcvData;
            DatagramSocket socket = new DatagramSocket(4567);
            DatagramPacket rcvNotif = new DatagramPacket(data, data.length);
            //we receive the model (either notification or validation)
            while (true) { //the thread will be in a receiving state constantly
                try {
                    System.out.println("Waiting for Broadcast");
                    socket.receive(rcvNotif);
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                if (rcvNotif.getLength() == 0) {
                    System.out.println("Read zero bytes");
                }
                else if (rcvNotif.getAddress().getHostName()==UserManager.user_self.get_Hostname()){
                    System.out.println("Broadcasted to myself");
                }
                else{
                    rcvData = new String(rcvNotif.getData());
                    //we need to check if notification or validation... we split the string that we received with "-" as a delimiter. If we only have 1 element then that means it's a notification because it does not contain the boolean, if we have 2 elements that means it's a validation
                    String[] splitString = rcvData.split("-");
                    if(splitString.length==1){
                        this.notif=new Notifications(new User(rcvNotif.getAddress().getHostName(),socket.getPort()),splitString[0]);
                        NM.Receive_BC(this.notif);
                    }
                    else if(splitString.length==2){
                        this.valid=new Validation(new User(rcvNotif.getAddress().getHostName(),socket.getPort()),splitString[0],Boolean.parseBoolean(splitString[1]));
                        NM.Receive_BC(this.valid);
                    }
                    else{
                        System.out.println("Error there are 3 or more separate fields in the broadcast message");
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
