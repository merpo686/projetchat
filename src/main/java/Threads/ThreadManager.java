package Threads;
import ActivityManagers.Self;
import Models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.*;

/**This class contains all the managing-thread related functions */
public class ThreadManager implements Observers.ObserverReception {
    private static final Logger LOGGER = LogManager.getLogger(ThreadManager.class);
    private final Map<User,TCPClientHandler> map_active_conversations; //list of active conversations (active TCP threads)
    static ThreadManager instance;
    private final UDPServer udpServer;
    /**
     * Constructor
     */
    private ThreadManager() {
        map_active_conversations = new HashMap<>();
        udpServer = new UDPServer(Self.portUDP, true);
        udpServer.setDaemon(true);
        udpServer.start();
        StartTCPServer();
        SendConnection();
    }
    /**
     * @return the instance of ThreadManager
     */
    public static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }
    /**
     * @return the active ThreadRecvUDP
     */
    public UDPServer getUdpServer(){return udpServer;}
    /**
     * Add a thread to the list of active conversation threads
     * @param dest
     * @param thread
     */
    public void addActiveconversation(User dest, TCPClientHandler thread){map_active_conversations.put(dest,thread);}
    /**
     * Delete a thread to the list of active conversation threads
     * @param dest
     */
    public void delActiveconversation(User dest){
        TCPClientHandler thread = map_active_conversations.remove(dest);
        if (thread!=null){
            thread.interrupt();
        }
    }
    /**
     * @param dest
     * @return the conversation thread corresponding to the user, null if not exist
     */
    public TCPClientHandler getActiveconversation(User dest){return map_active_conversations.get(dest);}
    /** Closes all threads, active conversation and recv servers*/
    public void deleteAllThreads(){
        //close active conversations threads
        for (User dest : map_active_conversations.keySet()){
            delActiveconversation(dest);
        }
    }
    /**Start TCP server for accepting new conversations*/
    public void StartTCPServer() {
        TCPServer tcpServer= new TCPServer(Self.portTCP);
        tcpServer.setDaemon(true);
        tcpServer.start();
    }
    /***
     * Functions for Sending on UDP
     *
     * Sends true on Broadcast */
    public void SendConnection() {
        SendUDPBC("true", Self.portUDP);
    }
    /**Sends false on Broadcast */
    public static void SendDisconnection() {
        SendUDPBC("false", Self.portUDP);
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
        SendUDPBC(Self.getInstance().getPseudo(), Self.portUDP);
    }
    /**
     * Function to send messages in broadcast UDP
     * @param messageToSend
     */
    public static void SendUDPBC(String messageToSend, int portUDP){
        byte [] pseudoData = messageToSend.getBytes();
        LOGGER.debug("[ThreadSendBC] Sending "+messageToSend+" in SendThread");
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            DatagramPacket sendNotif;
            sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName("255.255.255.255"), portUDP);
            socket.send(sendNotif);
            socket.close();
        } catch (IOException e) {
            LOGGER.error("Failed to send broadcast message. Error with DatagramSoccket. Parameters: " +
                    "PortUDP="+portUDP+" Destination: 255.255.255.255");
            e.printStackTrace();
        }
    }
    /** Sends a username on UDP (non-broadcast)
     * @param hostname to send to
     * */
    static public void SendPseudoUnicast(String hostname, int portUDP) {
        byte[] pseudoData = (Self.getInstance().getPseudo()).getBytes();
        DatagramPacket sendNotif;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            sendNotif = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName(hostname), portUDP);
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
    @Override
    public void messageReceived(Message mess){
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
