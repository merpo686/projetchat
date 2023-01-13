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
/** Class containing all messages-related functions : sending, receiving */
public class NetworkManager {
    private static final Logger LOGGER = LogManager.getLogger(NetworkManager.class);
    /**Sends true on Broadcast */
    public static void SendConnection() {
        Connection connect = new Connection(Self.getInstance().getHostname(),true);
        ThreadManager.SendBC(connect);
    }
    /**Sends false on Broadcast */
    public static void SendDisconnection() {
        Connection connect = new Connection(Self.getInstance().getHostname(),false);
        ThreadManager.SendBC(connect);
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
        ThreadManager.SendBC(new User(Self.getInstance().getHostname(),Self.getInstance().getPseudo()));
    }
    /**Process boolean received on udp : either a connection(true) or a disconnection (false)
     * @param connect the Connection containing the boolean to send and the user whom sent
     * */
    public static void ProcessConnection(Connection connect){
        if (connect.getValid() && Self.getInstance().getPseudo()!=null){
            ThreadManager.SendPseudoUnicast(connect.getHostname()); //we received a connection notification, we respond our pseudo if we chose it
        }
        else {
            ActiveUserManager.getInstance().removeListActiveUser(connect.getHostname()); //we remove the user from the list of active user
            if (InterfaceManager.getInstance().getState().equals("ChatInterface") &&
                InterfaceManager.getInstance().getUser().getHostname().equals(connect.getHostname())){
                //if we are chatting with the user right now it shows a user-disconnected interface
                JFrame frame = new JFrame();
                String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
                try {
                    UIManager.setLookAndFeel(lookAndFeel);
                } catch (Exception e) {
                    LOGGER.error("Couldn't get specified look and feel ("
                            + lookAndFeel
                            + "), for some reason. Using the default look and feel.");
                    e.printStackTrace();
                }
                JFrame.setDefaultLookAndFeelDecorated(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JLabel label =new JLabel("<html>User disconnected, messages won't get through, <br>" +
                    "please return to choose discussion interface</html>");
                label.setHorizontalAlignment(SwingConstants.CENTER);
                frame.add(label);
                frame.setMinimumSize(new Dimension(350,70));
                frame.setForeground(InterfaceManager.foregroundColor);
                frame.setBackground(InterfaceManager.backgroundColor);
                frame.setLocationRelativeTo(null); //center of the screen
                frame.setVisible(true);
            }
            //delete the user from active conversations
            ThreadManager.getInstance().delActiveconversation(ActiveUserManager.getInstance().get_User(connect.getHostname()));
        }
    }
    /**Process a pseudo received on UDP
     * @param user we received the pseudo from
     * */
    public static void ProcessPseudo(User user) {
        ActiveUserManager.getInstance().changeListActiveUser(user);
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
