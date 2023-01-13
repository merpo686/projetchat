package Managers;

import Graphics.InterfaceManager;
import Models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.lang.*;
import java.io.IOException;
import java.net.*;
/** Class responsible for receiving UDP messages -  a thread receiving full time on a UDP socket,
 *  calling the subconsequent handler when there is a message */
public class UDPServer extends Thread {
    private static final Logger LOGGER = LogManager.getLogger(UDPServer.class);

    private Observer observer;
    public void attach(Observer observer){
        this.observer= observer;
    }
    public void notifyObserver(){
        observer.update();
    }

    /**
     * Constructor
     */
    public UDPServer(){
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
                    Connection connect;
                    connect = new Connection(rcvNotif.getAddress().getHostName(), Boolean.parseBoolean(rcvData));
                    LOGGER.trace("We received a connection/deconnection notification "+rcvData);
                    ProcessConnection(connect);
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
    public void ProcessConnection(Connection connect){
        if (connect.getValid() && Self.getInstance().getPseudo()!=null){
            NetworkManager.SendPseudoUnicast(connect.getHostname()); //we received a connection notification, we respond our pseudo if we chose it
        }
        else {
            ActiveUserManager.getInstance().removeListActiveUser(connect.getHostname()); //we remove the user from the list of active user
            if (InterfaceManager.getInstance().getState().equals("ChatInterface") &&
                    InterfaceManager.getInstance().getUser().getHostname().equals(connect.getHostname())){
                notifyObserver();
            }
            //delete the user from active conversations
            ThreadManager.getInstance().delActiveconversation(ActiveUserManager.getInstance().get_User(connect.getHostname()));
        }
    }
    /**Process a pseudo received on UDP
     * @param user we received the pseudo from
     * */
    public void ProcessPseudo(User user) {
        ActiveUserManager.getInstance().changeListActiveUser(user);
    }
}
