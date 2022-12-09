package Managers;

import Models.*;
import java.io.*;
import java.net.*;

public class ThreadSendBC implements Runnable {
    private final DatagramSocket socket;
    private Connection connect;
    private NotifPseudo notif;
    int numSocket;
    public ThreadSendBC(Connection connect) throws SocketException {
        this.socket = new DatagramSocket();
        this.connect=connect;
        this.numSocket=connect.get_numPort();
    }
    public ThreadSendBC(NotifPseudo notif) throws SocketException {
        this.socket = new DatagramSocket();
        this.notif=notif;
        this.numSocket=notif.get_numPort();
    }

    public void run() {
        String data;
        if (this.notif==null) {
            data = "Connection-" + Boolean.toString(this.connect.get_Valid());
        }
        else {
            data = this.notif.get_Pseudo();
        }
        byte [] pseudoData = data.getBytes();
        try {
            System.out.println("[ThreadSendBC] Sending "+data+" in SendThread");
            socket.setBroadcast(true);
            DatagramPacket sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName("255.255.255.255"), numSocket);
            socket.send(sendNotif);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
