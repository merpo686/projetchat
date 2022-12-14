package Managers;

import Models.Connection;
import Models.NotifPseudo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Thread_Send_Pseudo_Unicast implements Runnable {
    private final DatagramSocket socket;
    private NotifPseudo notif;
    int numSocket;

    public Thread_Send_Pseudo_Unicast(NotifPseudo notifPseudo) throws SocketException {
        this.socket = new DatagramSocket();
        this.notif = notifPseudo;
        this.numSocket = notifPseudo.get_numPort();
    }

    public void run() {
        String data;
            data = this.notif.get_Pseudo();
            byte[] pseudoData = data.getBytes();
            try {
                DatagramPacket sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                        InetAddress.getByName(String.valueOf(notif.get_Hostname())), numSocket);
                socket.send(sendNotif);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
