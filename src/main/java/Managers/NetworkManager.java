package Managers;
import Graphics.InterfaceManager;
import Models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
/** Class containing all functions related to sending messages */
public class NetworkManager {

    private static final Logger LOGGER = LogManager.getLogger(NetworkManager.class);


    /***
     * Functions for Sending on UDP
     *
     *
     * Sends true on Broadcast */
    public static void SendConnection() {
        SendUDPBC("true");
    }
    /**Sends false on Broadcast */
    public static void SendDisconnection() {
        SendUDPBC("false");
        ThreadManager.getInstance().deleteAllThreads();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.debug("Error putting the thread to sleep in disconnection.");
            e.printStackTrace();
        }
    }
    /**Sends our pseudo on broadcast*/
    public static void SendPseudo()  {
        SendUDPBC(Self.getInstance().getPseudo());
    }
    /**
     * Function to send messages in broadcast UDP
     * @param messageToSend
     */
    public static void SendUDPBC(String messageToSend){
        byte [] pseudoData = messageToSend.getBytes();
        LOGGER.debug("[ThreadSendBC] Sending "+messageToSend+" in SendThread");
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            DatagramPacket sendNotif = null;
            sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName("255.255.255.255"), Self.portUDP);
            socket.send(sendNotif);
            socket.close();
        } catch (IOException e) {
            LOGGER.error("Failed to send broadcast message. Error with DatagramSoccket. Parameters: " +
                    "PortUDP="+Self.portUDP+" Destination: 255.255.255.255");
            e.printStackTrace();
        }
    }
    /** Sends a username on UDP (non-broadcast)
     * @param hostname to send to
     * */
    static public void SendPseudoUnicast(String hostname) {
        byte[] pseudoData = (Self.getInstance().getPseudo()).getBytes();
        DatagramPacket sendNotif = null;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName(hostname), Self.portUDP);
            socket.send(sendNotif);
            socket.close();
        } catch (SocketException e){
            LOGGER.error("Failed to create a DatagramSocket.");
        }
        catch (UnknownHostException e) {
            LOGGER.error("Failed to get InetAddress for hostname: "+hostname+".");
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("Failed to send our pseudo on UDP (non-broadcast).");
            e.printStackTrace();
        }
    }

    /** Sends a message on TCP
  * @param mess to send, containing who to send to and the message
  * */
    public static void SendMessageTCP(Message mess){
        TCPClientHandler thread=ThreadManager.getInstance().getActiveconversation(mess.getReceiver());
        Socket socket;
        try {
            socket=thread.getSocket();
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(mess.getMessage());
        } catch (IOException e) {
            LOGGER.error("Unable to write the message on the outputStream.");
            e.printStackTrace();
        }
    }
}
