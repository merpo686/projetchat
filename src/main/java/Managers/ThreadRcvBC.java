package Managers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class ThreadRcvBC {
    UserManager um;

    public ThreadRcvBC(UserManager um) {
        this.um = um;
    }

    public void Receive() throws IOException {
        byte[] data = new byte[1024];
        String rcvData;
        DatagramSocket socket = new DatagramSocket();
        DatagramPacket rcvNotif = new DatagramPacket(data, data.length);
        //on recoit la notif
        socket.receive(rcvNotif);

        if (rcvNotif.getLength() == 0) {
            System.out.println("Read zero bytes");
        } else {
            rcvData = new String(rcvNotif.getData());
            System.out.println(rcvData);
        }
    }
}
