import Graphics.InterfaceManager;
import Managers.*;
import Models.Connection;
import Graphics.ChoosePseudoInterface;
import Models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    /** handler for the reception of UDP messages
     * @param notif either a Connection (boolean + user) or just a User
     * */
    static ThreadManager.NotifHandler handler = notif -> {
        if (notif instanceof Connection) {
            Connection connect =(Connection) notif;
            NetworkManager.ProcessConnection(connect);
        }
        else if (notif instanceof User) {
            User user = (User)notif;
            NetworkManager.ProcessPseudo(user);
        }
    };

    public static void main(String[] args) {
        LOGGER.info("Starting chat application. port UDP=12340; port TCP=12341");
        Launcher();
    }
    /** Start the background components in charge of running the application */
    public static void Launcher() {
        LOGGER.debug("Launching application.");
        ActiveUserManager.getInstance(); //initalise the active user list
        start_GraphicInterface(); //crystal clear
        ThreadManager.StartRcvThread(handler); //starts the thread which receives non-stop on UDP
        NetworkManager.SendConnection(); //sends a boolean (true) on broadcast to notify our connection
        ThreadManager.StartTCPServer(); //Start the TCP server waiting to open conversations
    }
 /** Function in charge of starting the Graphic Interface, which is afterwards self-sufficient*/
    public static void start_GraphicInterface(){
        LOGGER.debug("Launching interface");
        //create new frame
        JFrame frame = new JFrame();
        //Set the look and feel.
        String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            LOGGER.error("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason. Using the default look and feel.");
            e.printStackTrace();
        }
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.setVisible(false);
                NetworkManager.SendDisconnection();
                frame.dispose();
            }
        });
        //Display the window.
        frame.setMinimumSize(new Dimension(400,300));
        frame.setForeground(InterfaceManager.foregroundColor);
        frame.setBackground(InterfaceManager.backgroundColor);
        frame.setLocationRelativeTo(null); //center of the screen
        new ChoosePseudoInterface(frame); //first interface displayed
        frame.setVisible(true);
    }
}
