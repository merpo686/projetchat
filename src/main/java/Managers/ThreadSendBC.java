package Managers;

import Models.*;
import java.io.*;
import java.net.*;

public class ThreadSendBC implements Runnable {
    DatagramSocket socket;
    Validation val;
    int numSocket;
    public void ThreadSendBC(Validation val, int numSocket) throws SocketException {
        this.socket = new DatagramSocket();
        this.val = val;
        this.numSocket = numSocket;
    }

    public void run() {
        byte [] pseudoData = val.getPseudo().getBytes();
        try {
            socket.setBroadcast(true);
            DatagramPacket sendNotif = new DatagramPacket(pseudoData, pseudoData.length, InetAddress.getByName("255.255.255.255"), numSocket);
            socket.send(sendNotif);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
