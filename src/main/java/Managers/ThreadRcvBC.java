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
            while (true) {
                socket.receive(rcvNotif);

                if (rcvNotif.getLength() == 0) {
                    System.out.println("Read zero bytes");
                } else {
                    rcvData = new String(rcvNotif.getData());
                    System.out.println(rcvData);
                    //we need to check if notification or validation... we split the string that we received in 2, if we only have 1 element then
                    if (rcvData=="true"||rcvData=="false"){
                        this.valid=new Validation(new User(rcvNotif.getAddress(),socket.getPort()),"",Boolean.parseBoolean(rcvData));
                    }
                    else {
                        this.notif=new Notifications(new User(rcvNotif.getAddress(),socket.getPort()),rcvData);
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
