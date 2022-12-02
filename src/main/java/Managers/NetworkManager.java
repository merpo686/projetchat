package Managers;
import Managers.*;
import Models.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkManager {
    public static Boolean Pseudo_Correct=false;
    public static Boolean Response_received=false;
    public static String Connection() throws SocketException, InterruptedException, UnknownHostException {
        User user_self = UserMain.getInstance().Get_User();
        String PseudoChosen = null;
        while (!Pseudo_Correct) {
            PseudoChosen= Ask_Pseudo();
            NetworkManager.Send_Pseudo(user_self, PseudoChosen);
            Thread.sleep(1000);
            if (!Response_received){ //we are the first user -> no response
                Pseudo_Correct=true;
            }
        }
        return PseudoChosen;
    }

    public static void Send_Pseudo(User user_self, String PseudoChosen) throws SocketException {
        NotifPseudo notifpseudo = new NotifPseudo(user_self,PseudoChosen);
        ThreadManager.Send_BC(notifpseudo);
    }

    public static String Ask_Pseudo(){
        System.out.println("[UserManager] Please enter a pseudo");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static void Process_Validation(Validation valid){
        if (valid.get_Valid()){
            Response_received=true;
            Pseudo_Correct=true;
        }
    }

    public static void Process_Notif_Pseudo(NotifPseudo notif) throws SocketException, UnknownHostException {
        String pseudo = UserMain.getInstance().Get_Pseudo();
        if (ActiveUserManager.getInstance().IsinActiveListUser(notif.get_Pseudo())){
            ThreadManager.Send_BC(new Validation(notif.get_User(), pseudo,false));
        }
        else {
            ThreadManager.Send_BC(new Validation(notif.get_User(),pseudo,true));
            ActiveUserManager.getInstance().addListActiveUser(notif.get_User());
        }
    }

}
