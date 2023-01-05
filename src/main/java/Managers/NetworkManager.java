package Managers;
import Graphics.ChatInterface;
import Graphics.ChooseDiscussionInterface;
import Graphics.InterfaceManager;
import Managers.*;
import Models.*;

import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class NetworkManager {
    public static Boolean Pseudo_Correct=false;
    public static Boolean Response_received=false;

    public static void Send_Connection() throws SocketException, UnknownHostException {
        Connection connect = new Connection(Self.getInstance().get_User(),true);
        ThreadManager.Send_BC(connect);
    }
    public static void Send_Disconnection() throws SocketException, UnknownHostException {
        Connection connect = new Connection(Self.getInstance().get_User(),false);
        ThreadManager.Send_BC(connect);
    }

    public static void Send_Pseudo( String PseudoChosen) throws SocketException, UnknownHostException {
        NotifPseudo notifpseudo = new NotifPseudo(Self.getInstance().get_User(),PseudoChosen);
        ThreadManager.Send_BC(notifpseudo);
    }

    public static String Ask_Pseudo(){
        System.out.println("[UserManager] Please enter a pseudo");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void Process_Connection(Connection connect) throws UnknownHostException, SocketException {
        //true=connection; false=deconnection
        if (connect.get_Valid()){
            ThreadManager.Send_Pseudo_Unicast(new NotifPseudo(connect.get_User(), Self.getInstance().get_Pseudo()));
        }
        else {
            ActiveUserManager.getInstance().removeListActiveUser(connect.get_User());
            if (InterfaceManager.getInstance().get_state().equals("ChatInterface") &&
                    InterfaceManager.getInstance().get_user()== connect.get_User()){
                    JFrame frame = new JFrame();
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
                    JLabel label =new JLabel("<html>User disconnected, messages won't get through, <br>" +
                        "please return to choose discussion interface</html>");
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    frame.add(label);

                    frame.setMinimumSize(new Dimension(350,70));
                    frame.setForeground(InterfaceManager.foreground_color);
                    frame.setBackground(InterfaceManager.background_color);
                    frame.setLocationRelativeTo(null); //center of the screen
                    frame.setVisible(true);
            }
        }
    }

    public static void Process_Notif_Pseudo(NotifPseudo notif) throws SocketException, UnknownHostException {
        ActiveUserManager.getInstance().addListActiveUser(notif.get_User());
    }

    public static void Send_Message_TCP(Message mess) throws IOException {
        TCPClientHandler thread=ThreadManager.getInstance().get_active_conversation(mess.get_receiver());
        Socket numPort;
        if (thread!=null){
            numPort=thread.getNumPort();
        }
        else{
            numPort= new Socket(InetAddress.getLocalHost(),mess.get_receiver().get_Port());
        }
        DataOutputStream outputStream = new DataOutputStream(numPort.getOutputStream());
        outputStream.writeUTF(mess.get_message());
    }

}
