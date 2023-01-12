package Managers;

import Models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;

/** Thread to send a message in UDP Broadcast */
public class ThreadSendBC implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(ThreadSendBC.class);
    private final DatagramSocket socket;
    private Connection connect;
    private User user;
    int numSocket;

    /**
     * Constructor when sending a boolean broadcast
     * @param connect
     * @throws SocketException
     */
    public ThreadSendBC(Connection connect) throws SocketException {
        this.socket = new DatagramSocket();
        this.connect=connect;
        this.numSocket=Self.portUDP;
    }

    /**
     * Constructor when sending a pseudo broadcast
     * @param user
     * @throws SocketException
     */
    public ThreadSendBC(User user) throws SocketException {
        this.socket = new DatagramSocket();
        this.user = user;
        this.numSocket=Self.portUDP;
    }
/** According to the type of message to send in broadcast - a pseudo or a boolean -;
 * open a UDP socket and send it in broadcast mode
  */
    public void run() {
        String data;
        if (this.user==null) {
            data = Boolean.toString(this.connect.getValid());
        }
        else {
            data = this.user.getPseudo();
        }
        byte [] pseudoData = data.getBytes();
        LOGGER.debug("[ThreadSendBC] Sending "+data+" in SendThread");
        try {
            socket.setBroadcast(true);
        } catch (SocketException e) {
            LOGGER.error("Failed to set Socket to Broadcast.");
            e.printStackTrace();
        }
        DatagramPacket sendNotif = null;
        try {
            sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName("255.255.255.255"), numSocket);
        } catch (UnknownHostException e) {
            LOGGER.error("Failed to get InetAddress for 255.255.255.255.");
            e.printStackTrace();
        }
        try {
            socket.send(sendNotif);
        } catch (IOException e) {
            LOGGER.error("Failed to send message.");
            e.printStackTrace();
        }
        socket.close();
    }
}
