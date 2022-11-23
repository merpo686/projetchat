package Managers;

import Models.Validation;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

public class ThreadSendBC implements Runnable {
    DatagramSocket socket;
    Validation val;
    public void ThreadSendBC(Validation val) throws SocketException {
        this.socket = new DatagramSocket();
        this.val = val;
    }

    public void run() {
        byte [] pseudoData = val.pseudo.getBytes();
        try {
            socket.setBroadcast(true);
            DatagramPacket sendNotif = new DatagramPacket(pseudoData, pseudoData.length, InetAddress.getByName("255.255.255.255"), socket);
            socket.send(sendNotif);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
