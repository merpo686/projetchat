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
public class ThreadManager implements Observers.ObserverMessage, Observers.ObserverConnection, Observers.ObserverDisconnection {
    private static final Logger LOGGER = LogManager.getLogger(ThreadManager.class);
    private final Map<User, TCPClient> map_active_conversations; //list of active conversations (active TCP threads)
    static ThreadManager instance;
    public static int portUDP;
    public static int portTCP;
    /**
     * Constructor
     */
    private ThreadManager() {
        map_active_conversations = new HashMap<>();
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
     * Add a thread to the list of active conversation threads
     * @param dest User with whom the thread is opened
     * @param thread TCPClient thread
     */
    public synchronized void addActiveThread(User dest, TCPClient thread){map_active_conversations.put(dest,thread);}
    /**
     * Delete a thread to the list of active conversation threads
     * @param dest User with whom the thread is opened
     */
    public synchronized void delActiveThread(User dest){
        TCPClient thread = map_active_conversations.remove(dest);
        if (thread!=null){
            thread.interrupt();
        }
    }
    /**
     * @param dest User with whom the thread is opened
     * @return the conversation thread corresponding to the user, null if not exist
     */
    public synchronized TCPClient getActiveThread(User dest){return map_active_conversations.get(dest);}
    /** Closes all threads, active conversation and receiving servers*/
    public synchronized void deleteAllThreads(){
        //close active conversations threads
        for (User dest : map_active_conversations.keySet()){
            delActiveThread(dest);
        }
    }

    public void notifyDisconnection(User user){
        delActiveThread(user);
    }
    /**
     * Starts the TCP server
     * @param portTCP port of server
     * @param handlerTCP handler when a new connection is detected
     */
    public static void StartTCPServer(int portTCP, HandlerTCP handlerTCP) {
        TCPServer tcpServer= new TCPServer(portTCP,handlerTCP);
        tcpServer.setDaemon(true);
        tcpServer.start();
    }

    /**
     * Starts the UDP server
     * @param portUDP port of server
     * @param handlerUDP handler when a message udp is received
     */
    public static void StartUDPServer(int portUDP,HandlerUDP handlerUDP, boolean ignoreSelf){
        UDPServer udpServer = new UDPServer(portUDP, ignoreSelf, handlerUDP);
        udpServer.setDaemon(true);
        udpServer.start();
    }
    /***
     * Functions for Sending on UDP
     *
     * Sends true on Broadcast */
    public static void SendConnection() {
        SendUDPBC("true", portUDP);
    }

    /**
     * Sends false on Broadcast
     * @param context - if 0, we are in the main program, so we exit. Else, we are in test mode so we don't exit
     */
    public static void SendDisconnection(int context) {
        SendUDPBC("false", portUDP);
        ThreadManager.getInstance().deleteAllThreads();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            LOGGER.debug("Error putting the thread to sleep in disconnection.");
            e.printStackTrace();
        }
        if (context == 0){
            System.exit(0);
        }
    }
    /**Sends our pseudo on broadcast*/
    public static void SendPseudo(String pseudo)  {
        SendUDPBC(pseudo,portUDP);
    }
    /**
     * Function to send messages in broadcast UDP
     * @param messageToSend String to be sent - either true,false, or a pseudo in our app
     */
    public static void SendUDPBC(String messageToSend, int portUDP){
        byte [] pseudoData = messageToSend.getBytes();
        LOGGER.debug("[ThreadSendBC] Sending "+messageToSend+" in SendThread");
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            DatagramPacket sendPacket;
            sendPacket = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName("255.255.255.255"), portUDP);
            socket.send(sendPacket);
            socket.close();
        } catch (IOException e) {
            LOGGER.error("Failed to send broadcast message. Error with DatagramSocket. Parameters: " +
                    "PortUDP="+portUDP+" Destination: 255.255.255.255");
            e.printStackTrace();
        }
    }
    /** Sends a username on UDP (non-broadcast)
     * @param hostname to send to
     * */
    static public void SendPseudoUnicast(String hostname, int portUDP) {
        byte[] pseudoData = (Self.getInstance().getPseudo()).getBytes();
        DatagramPacket sendPacket;
        DatagramSocket socket;
        try {
            socket = new DatagramSocket();
            sendPacket = new DatagramPacket(pseudoData, pseudoData.length,
                    InetAddress.getByName(hostname), portUDP);
            socket.send(sendPacket);
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
    public void messageHandler(Message mess){
        User dest =mess.getReceiver();
        TCPClient tcpClientHandler  = ThreadManager.getInstance().getActiveThread(dest);
        Socket socket = null;
        if (tcpClientHandler==null) {
            try {
                socket = new Socket(dest.getHostname(),portTCP);
            } catch (IOException e) {
                LOGGER.debug("Unable to create TCP socket. Hostname: " + dest.getHostname() + " Port TCP: " + portTCP);
                e.printStackTrace();
                LOGGER.info("Please return to choose discussion window, user certainly disconnected.");
            }
            tcpClientHandler = new TCPClient(socket, dest);
            tcpClientHandler.start();
            ThreadManager.getInstance().addActiveThread(dest, tcpClientHandler);
        }
        socket = tcpClientHandler.getSocket();
        DataOutputStream outputStream;
        try {
            outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(mess.getMessage());
        } catch (IOException e) {
            LOGGER.debug("Unable to send the message via TCP.");
            e.printStackTrace();
        }
    }

    @Override
    public void userConnected(User user) {
        SendPseudo(user.getPseudo());
    }

    @Override
    public void userDisconnected(User user) {
        SendDisconnection(0);
    }
}
