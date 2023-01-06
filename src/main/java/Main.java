import Graphics.InterfaceManager;
import Managers.*;
import Models.Connection;
import Graphics.ChoosePseudoInterface;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {

    static ThreadManager.NotifHandler handler = notif -> {
        if (notif instanceof Connection) {
            Connection connect =(Connection) notif;
            NetworkManager.Process_Connection(connect);
        }
        else if (notif instanceof User) {
            User user = (User)notif;
            NetworkManager.Process_Notif_Pseudo(user);
        }
    };

    public static void main(String[] args) {
        try {
            Launcher();
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

    public static void Launcher() throws IOException {
        ActiveUserManager AUM = ActiveUserManager.getInstance();
        start_GraphicInterface();
        ThreadManager.Start_RcvThread(handler);
        NetworkManager.Send_Connection();
        ThreadManager.Start_TCP_Server();
    }

    public static void start_GraphicInterface(){

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

        frame.setMinimumSize(new Dimension(400,300));
        frame.setForeground(InterfaceManager.foreground_color);
        frame.setBackground(InterfaceManager.background_color);
        frame.setLocationRelativeTo(null); //center of the screen
        ChoosePseudoInterface IT= new ChoosePseudoInterface(frame); //first interface displayed
        frame.setVisible(true);
    }
}
