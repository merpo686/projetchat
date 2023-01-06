package Managers;
import Graphics.InterfaceManager;
import Models.*;
import javax.swing.*;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class NetworkManager {
    public static void Send_Connection() throws SocketException, UnknownHostException {
        Connection connect = new Connection(Self.getInstance().getHostname(),true);
        ThreadManager.Send_BC(connect);
    }
    public static void Send_Disconnection() throws SocketException, UnknownHostException {
        Connection connect = new Connection(Self.getInstance().getHostname(),false);
        ThreadManager.Send_BC(connect);
    }

    public static void Send_Pseudo() throws SocketException, UnknownHostException {
        ThreadManager.Send_BC(new User(Self.getInstance().getHostname(),Self.getInstance().get_Pseudo()));
    }
    public static void Process_Connection(Connection connect) throws UnknownHostException, SocketException {
        //true=connection; false=deconnection
        if (connect.getValid()){
            ThreadManager.Send_Pseudo_Unicast(connect.getHostname());
        }
        else {
            ActiveUserManager.getInstance().removeListActiveUser(connect.getHostname());
            if (InterfaceManager.getInstance().get_state().equals("ChatInterface") &&
                    InterfaceManager.getInstance().get_user().get_Hostname().equals(connect.getHostname())){
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
                    ThreadManager.getInstance().del_active_conversation(ActiveUserManager.getInstance().get_User(connect.getHostname()));
            }
        }
    }

    public static void Process_Notif_Pseudo(User user) {
        ActiveUserManager.getInstance().changeListActiveUser(user);
    }

    public static void Send_Message_TCP(Message mess) throws IOException {
        TCPClientHandler thread=ThreadManager.getInstance().get_active_conversation(mess.get_receiver());
        Socket socket;
        if (thread!=null){
            socket=thread.getNumPort();
        }
        else{
            socket= new Socket(InetAddress.getLocalHost(),Self.portTCP);
            thread = new TCPClientHandler(socket,mess.get_receiver());
            thread.start();
            ThreadManager.getInstance().add_active_conversation(mess.get_receiver(),thread);

        }
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        outputStream.writeUTF(mess.get_message());
    }

}
