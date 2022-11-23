package Managers;

import Models.*;
import java.lang.*;
import java.io.IOException;
import java.net.*;

public class ThreadRcvBC implements Runnable {
    NetworkManager NM;
    Validation valid;
    Notifications notif;
    public ThreadRcvBC(NetworkManager NM) {
        this.NM = NM;
    }

    public void run() {
        try {
            byte[] data = new byte[1024];
            String rcvData;
            DatagramSocket socket = new DatagramSocket();
            DatagramPacket rcvNotif = new DatagramPacket(data, data.length);
            //we receive the model (either notification or validation)
            while (true) { //the thread will be in a receiving state constantly
                socket.receive(rcvNotif);

                if (rcvNotif.getLength() == 0) {
                    System.out.println("Read zero bytes");
                } else {
                    rcvData = new String(rcvNotif.getData());
                    System.out.println(rcvData);
                    //we need to check if notification or validation... we split the string that we received with "-" as a delimiter. If we only have 1 element then that means it's a notification because it does not contain the boolean, if we have 2 elements that means it's a validation
                    String[] splitString = rcvData.split("-");
                    if(splitString.length==1){
                        this.notif=new Notifications(new User(rcvNotif.getAddress(),socket.getPort()),splitString[0]);
                        NM.Receive_BC(this.notif);
                    }
                    else if(splitString.length==2){
                        this.valid=new Validation(new User(rcvNotif.getAddress(),socket.getPort()),splitString[0],Boolean.parseBoolean(splitString[1]));
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
