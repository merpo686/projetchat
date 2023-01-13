package Threads;

import Graphics.InterfaceManager;
import Models.*;
import ActivityManagers.ActiveUserManager;
import Models.ObserverDisconnection;
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
    private static final Logger LOGGER = LogManager.getLogger(UDPServer.class);

    private ArrayList<ObserverDisconnection> observers = new ArrayList<>();
    public void attach(ObserverDisconnection observer){
        this.observers.add(observer);
    }
    public void notifyObserver(){
        for(ObserverDisconnection observer :observers){
            observer.update();
        }
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
                LOGGER.debug("Waiting for UDP message");
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
            ThreadManager.SendPseudoUnicast(hostname); //we received a connection notification, we respond our pseudo if we chose it
        }
        else {
            ActiveUserManager.getInstance().removeListActiveUser(hostname); //we remove the user from the list of active user
            if (InterfaceManager.getInstance().getState().equals("ChatInterface") &&
                    InterfaceManager.getInstance().getUser().getHostname().equals(hostname)){
                notifyObserver();
            }
            //delete the user from active conversations
            ThreadManager.getInstance().delActiveconversation(ActiveUserManager.getInstance().get_User(hostname));
        }
    }
    /**Process a pseudo received on UDP
     * @param user we received the pseudo from
     * */
    public void ProcessPseudo(User user) {
        ActiveUserManager.getInstance().changeListActiveUser(user);
    }
}
