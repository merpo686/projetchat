import Graphics.InterfaceManager;
import ActivityManagers.*;
import Graphics.ChoosePseudoInterface;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        LOGGER.info("Starting chat application. port UDP=12340; port TCP=12341");
        Launcher();
    }
    /** Start the background components in charge of running the application */
    public static void Launcher() {
        LOGGER.debug("Launching application.");
        ActiveUserManager.getInstance(); //initalise the active user list
        start_GraphicInterface(); //crystal clear
        ThreadManager.getInstance(); //starts all reception threads, and send true (connected) to others
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
                ThreadManager.SendDisconnection();
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
