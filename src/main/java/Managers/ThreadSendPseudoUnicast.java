package Managers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.*;

/** Thread called when sending our pseudo to someone */
public class ThreadSendPseudoUnicast implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ThreadSendPseudoUnicast.class);
    private final DatagramSocket socket;
    int numSocket;
    private final String hostname;

    public ThreadSendPseudoUnicast(String hostname) throws SocketException {
        this.socket= new DatagramSocket();
        this.numSocket=Self.portUDP;
        this.hostname=hostname;
    }

    public void run() {
        String data;
        data = Self.getInstance().getPseudo();
        byte[] pseudoData = data.getBytes();
        DatagramPacket sendNotif = null;
        try {
            sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName(hostname), numSocket);
        } catch (UnknownHostException e) {
            LOGGER.error("Failed to get InetAddress for hostname "+hostname);
            e.printStackTrace();
        }
        try {
            socket.send(sendNotif);
        } catch (IOException e) {
            LOGGER.error("Failed to send our pseudo on UDP unicast. Receiver: "+hostname);
            e.printStackTrace();
        }
        socket.close();
    }
}
