import Conversations.ConversationsManager;
import Graphics.Interface;
import ActivityManagers.*;
import Models.*;
import Threads.TCPClient;
import Threads.ThreadManager;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;


public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    static int portUDP=12340;
    static int portTCP=12341;

    static ThreadManager threadManager;
    static Interface graphics;
    static ConversationsManager conversationsManager;
    static ActiveUserManager activeUserManager;
    static Self self;
    /**
     * Handler for the UDP server, manages the different messages received: a connection/disconnection, a pseudo
     */
    static HandlerUDP handlerUDP = new HandlerUDP() {
        @Override
        public void handle(String hostname, String message) {
            if (message.equals("true")||message.equals("false")) {
                LOGGER.debug("We received a connection/disconnection notification "+message);
                if (Boolean.parseBoolean(message) && Self.getInstance().getPseudo()!=null){
                    ThreadManager.SendPseudoUnicast(hostname, portUDP); //we received a connection notification, we respond our pseudo if we chose it
                }
                else {
                    User user = ActiveUserManager.getInstance().removeListActiveUser(hostname); //we remove the user from the list of active user
                    if (user == null){
                        LOGGER.debug("we didn't knew this user was connected. His disconnection is thus irrelevant");
                    }
                    else{
                        graphics.userDisconnected(user);
                        threadManager.notifyDisconnection(user);
                    }
                }
            } else {
                User user = new User(hostname, message);
                LOGGER.debug("We received a username/pseudo "+message);
                ActiveUserManager.getInstance().changeListActiveUser(user);
                graphics.userConnected(user);
            }
        }
    };
    /**
     * Handler of the TCPServer, creates a new Thread for the tcp conversation, adds it to the list of threads
     */
    static HandlerTCP handlerTCP = link -> {
        User dest = ActiveUserManager.getInstance().get_User(link.getInetAddress().getHostName());
        TCPClient thread = new TCPClient(link,dest); //creating the conversation receiving thread
        thread.start();
        ThreadManager.getInstance().addActiveThread(dest,thread); //adding the thread to the list of active conversations
    };
    /**
     * Handler to be passed to TCPClient when created, will notify the reception of a new message
     */
    static HandlerMessageReceived handlerMessageReceived = new HandlerMessageReceived() {
        @Override
        public void handle(Message mess) {
            graphics.messageHandler(mess);
            conversationsManager.messageHandler(mess);
        }
    };

    public static void main(String[] args) {
        Configurator.setRootLevel(Level.INFO);
        LOGGER.info("Starting chat application. port UDP=12340; port TCP=12341");
        //First we initialize our instances
        activeUserManager = ActiveUserManager.getInstance();
        self =Self.getInstance();
        conversationsManager = ConversationsManager.getInstance();
        graphics = new Interface();
        threadManager = ThreadManager.getInstance();
        //Then we put the static parameters
        ThreadManager.portUDP =portUDP;
        ThreadManager.portTCP= portTCP;
        TCPClient.handlerMessageReceived = handlerMessageReceived;
        //We start the reception threads
        ThreadManager.StartUDPServer(portUDP,handlerUDP, true);
        ThreadManager.StartTCPServer(portTCP,handlerTCP);
        //We attach the observers
        graphics.attachMess(threadManager);
        graphics.attachMess(conversationsManager);
        graphics.attachConnection(threadManager);
        graphics.attachConnection(self);
        graphics.attachDisconnection(threadManager);
        //We start our connection by sending "true" on udp broadcast
        ThreadManager.SendConnection();
    }
}
