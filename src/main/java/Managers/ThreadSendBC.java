package Managers;

import Models.*;
import java.io.*;
import java.net.*;

public class ThreadSendBC implements Runnable {
    private final DatagramSocket socket;
    private Validation valid;
    private NotifPseudo notif;
    int numSocket;
    public ThreadSendBC(Validation valid) throws SocketException {
        this.socket = new DatagramSocket();
        this.valid = valid;
        this.numSocket=valid.get_numPort();
    }
    public ThreadSendBC(NotifPseudo notif) throws SocketException {
        this.socket = new DatagramSocket();
        this.notif=notif;
        this.numSocket=notif.get_numPort();
    }

    public void run() {
        String data;
        if (this.notif==null) {
            data = this.valid.get_Pseudo() + "-" + this.valid.get_Valid();
            byte [] pseudoData = data.getBytes();
            try {
                DatagramPacket sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                        InetAddress.getByName(String.valueOf(valid.get_Hostname())), numSocket);
                socket.send(sendNotif);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            data = this.notif.get_Pseudo();
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
}
