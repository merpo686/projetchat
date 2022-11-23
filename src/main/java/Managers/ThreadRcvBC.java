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
            //on recoit la notif
            while (true) {
                socket.receive(rcvNotif);

                if (rcvNotif.getLength() == 0) {
                    System.out.println("Read zero bytes");
                } else {
                    rcvData = new String(rcvNotif.getData());
                    System.out.println(rcvData);
                    if (rcvData=="true"||rcvData=="false"){
                        this.valid=new Validation(new User(rcvNotif.getAddress(),socket.getPort()),"",Boolean.parseBoolean(rcvData));
                    }
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
