package Managers;
import Managers.*;
import Models.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkManager {
    public static Boolean Pseudo_Correct=false;
    public static Boolean Response_received=false;

    public static void Send_Connection() throws SocketException, UnknownHostException {
        Connection connect = new Connection(UserMain.getInstance().Get_User(),true);
        ThreadManager.Send_BC(connect);
    }
    public static void Send_Disconnection() throws SocketException, UnknownHostException {
        Connection connect = new Connection(UserMain.getInstance().Get_User(),false);
        ThreadManager.Send_BC(connect);
    }

    public static void Send_Pseudo( String PseudoChosen) throws SocketException, UnknownHostException {
        NotifPseudo notifpseudo = new NotifPseudo(UserMain.getInstance().Get_User(),PseudoChosen);
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
            ThreadManager.Send_Pseudo_Unicast(new NotifPseudo(connect.get_User(), UserMain.getInstance().Get_Pseudo()));
        }
        else {
            ActiveUserManager.getInstance().removeListActiveUser(connect.get_User());
        }
    }

    public static void Process_Notif_Pseudo(NotifPseudo notif) throws SocketException, UnknownHostException {
        ActiveUserManager.getInstance().addListActiveUser(notif.get_User());
    }

}
