package Threads;

import Models.*;
import ActivityManagers.Self;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.*;
import java.io.IOException;
import java.net.*;

/** Class responsible for receiving UDP messages -  a thread receiving full time on a UDP socket,
 *  calling the subconsequent handler when there is a message */
public class UDPServer extends Thread {
    private final int portUDP;
    boolean ignoreSelfBC;
    private static final Logger LOGGER = LogManager.getLogger(UDPServer.class);
    private final HandlerUDP handlerUDP;
    public UDPServer(int portUDP, boolean ignoreSelfBC,HandlerUDP handlerUDP){
        this.portUDP=portUDP;
        this.ignoreSelfBC = ignoreSelfBC;
        this.handlerUDP =handlerUDP;
    }
    public void run() {
        byte[] data = new byte[1024];
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(portUDP);
        } catch (SocketException e) {
            LOGGER.error("Failed to bound UDP socket to UDP port.");
            e.printStackTrace();
        }
        DatagramPacket rcvNotif = new DatagramPacket(data, data.length);
        //we receive the model (either notification or validation)
        while (true) { //the thread will be in a receiving state constantly
            try {
                LOGGER.debug("Waiting for UDP message");
                socket.receive(rcvNotif);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (rcvNotif.getLength() == 0) {
                LOGGER.debug("[ThreadRcvBC] Read zero bytes");
            } else if (!ignoreSelfBC || !(rcvNotif.getAddress().getHostName().contains(Self.getInstance().getHostname()))){
                //we do not check messages received from ourselves (we added a boolean 'ignoreSelfBC' so that the condition be validated when we are testing)
                String rcvData = new String(rcvNotif.getData(), 0, rcvNotif.getLength());
                handlerUDP.handle(rcvNotif.getAddress().getHostName(),rcvData);
            }
        }
    }
}
