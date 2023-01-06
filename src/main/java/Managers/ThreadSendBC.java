package Managers;

import Models.*;
import java.io.*;
import java.net.*;

public class ThreadSendBC implements Runnable {
    private final DatagramSocket socket;
    private Connection connect;
    private User user;
    int numSocket;
    public ThreadSendBC(Connection connect) throws SocketException {
        this.socket = new DatagramSocket();
        this.connect=connect;
        this.numSocket=Self.portUDP;
    }
    public ThreadSendBC(User user) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.user = user;
        this.numSocket=Self.portUDP;
    }

    public void run() {
        String data;
        if (this.user==null) {
            data = Boolean.toString(this.connect.getValid());
        }
        else {
            data = this.user.get_Pseudo();
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
