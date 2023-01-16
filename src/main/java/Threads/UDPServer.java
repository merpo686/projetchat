package Threads;

import Models.*;
import ActivityManagers.ActiveUserManager;
import ActivityManagers.Self;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/** Class responsible for receiving UDP messages -  a thread receiving full time on a UDP socket,
 *  calling the subconsequent handler when there is a message */
public class UDPServer extends Thread {
    private int portUDP;
    boolean ignoreSelfBC;
    private static final Logger LOGGER = LogManager.getLogger(UDPServer.class);
    private final ArrayList<Observers.ObserverDisconnection> observers = new ArrayList<>();

    public UDPServer(int portUDP, boolean ignoreSelfBC){
        this.portUDP=portUDP;
        this.ignoreSelfBC = ignoreSelfBC;
    }

    public void attachDisconnection(Observers.ObserverDisconnection observer){
        this.observers.add(observer);
    }
    public void notifyObserverDisconnection(User user){
        for(Observers.ObserverDisconnection observer :observers){
            observer.userDisconnected(user);
        }
    }
    private Observers.ObserverConnection observer;
    public void attachConnection(Observers.ObserverConnection observer){
        this.observer= observer;
    }
    public void notifyObserverConnection(User user){
        try{
            observer.userConnected(user);
        }
        catch (NullPointerException e){
            LOGGER.debug("Not on the change discussion interface.");
        }
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
                //we check the type of message received, wether it is a Boolean or a pseudo
                if (rcvData.equals("true")||rcvData.equals("false")) {
                    LOGGER.trace("We received a connection/deconnection notification "+rcvData);
                    ProcessConnection(Boolean.parseBoolean(rcvData),rcvNotif.getAddress().getHostName());
                } else {
                    User user = new User(rcvNotif.getAddress().getHostName(), rcvData);
                    LOGGER.trace("We received a username/pseudo "+rcvData);
                    ProcessPseudo(user);
                }
            }
        }
    }
    /**Process boolean received on udp : either a connection(true) or a disconnection (false)
     * @param connect the Connection containing the boolean to send and the user whom sent
     * */
    public void ProcessConnection(Boolean connect, String hostname){
        if (connect && Self.getInstance().getPseudo()!=null){
            ThreadManager.SendPseudoUnicast(hostname, portUDP); //we received a connection notification, we respond our pseudo if we chose it
        }
        else {
            User user = ActiveUserManager.getInstance().removeListActiveUser(hostname); //we remove the user from the list of active user
            notifyObserverDisconnection(user);
        }
    }
    /**Process a pseudo received on UDP
     * @param user we received the pseudo from
     * */
    public void ProcessPseudo(User user) {
        ActiveUserManager.getInstance().changeListActiveUser(user);
        notifyObserverConnection(user);
    }
}
