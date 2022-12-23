package Graphics;

/*
 * SwingApplication.java is a 1.4 example that requires
 * no other files.
 */
import Managers.NetworkManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.NumberFormatter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.net.URL;

public class InterfaceManager {

    public static Color background_color = new Color(15,5, 107);
    public static Color foreground_color = new Color(252,210,28);

    public static void main(String[] args) {
        //create new frame
        JFrame frame = new JFrame();
        //Set the look and feel.
        String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.err.println("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason.");
            System.err.println("Using the default look and feel.");
            e.printStackTrace();
        }
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Display the window.
        frame.pack();
        frame.setMinimumSize(new Dimension(400,300));
        frame.setForeground(foreground_color);
        frame.setBackground(background_color);
        frame.setLocationRelativeTo(null); //center of the screen
        ChoosePseudoInterface IT= new ChoosePseudoInterface(frame); //first interface displayed
        frame.setVisible(true);
    }
}
