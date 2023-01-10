package Managers;

import Models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.*;
import java.io.IOException;
import java.net.*;
/** Class responsible for receiving UDP messages -  a thread receiving full time on a UDP socket,
 *  calling the subconsequent handler when there is a message */
public class ThreadRcvUDP extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(ThreadRcvUDP.class);
    private final ThreadManager.NotifHandler notifHandler;
    public ThreadRcvUDP(ThreadManager.NotifHandler handler) {
        this.notifHandler=handler;
    }

    public void run() {
        byte[] data = new byte[1024];
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(Self.portUDP);
        } catch (SocketException e) {
            LOGGER.error("Failed to bound UDP socket to UDP port.");
            e.printStackTrace();
        }
        DatagramPacket rcvNotif = new DatagramPacket(data, data.length);
        //we receive the model (either notification or validation)
        while (true) { //the thread will be in a receiving state constantly
            try {
                LOGGER.debug("[ThreadRcvBC] Waiting for Broadcast");
                socket.receive(rcvNotif);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (rcvNotif.getLength() == 0) {
                LOGGER.debug("[ThreadRcvBC] Read zero bytes");
            } else if (!(rcvNotif.getAddress().getHostName().contains(Self.getInstance().getHostname()))){ //we do not check messages received from ourselves
                String rcvData = new String(rcvNotif.getData(), 0, rcvNotif.getLength());
                //we check the type of message received, wether it is a Boolean or a pseudo
                if (rcvData.equals("true")||rcvData.equals("false")) {
                    Connection connect;
                    connect = new Connection(rcvNotif.getAddress().getHostName(), Boolean.parseBoolean(rcvData));
                    LOGGER.trace("We received a connection/deconnection notification "+rcvData);
                    this.notifHandler.handler(connect);
                } else {
                    User user = new User(rcvNotif.getAddress().getHostName(), rcvData);
                    LOGGER.trace("We received a username/pseudo "+rcvData);
                    this.notifHandler.handler(user);
                }
            }
        }
    }
}
