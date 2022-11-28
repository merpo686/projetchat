package Managers;
import Managers.*;
import Models.*;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NetworkManager {
    public static Boolean Pseudo_Correct;
    public NetworkManager(){
        this.Pseudo_Correct=false;
    }

    public static String Connection(User user_self){
        String PseudoChosen;
        while (!Pseudo_Correct) {
            PseudoChosen= Ask_Pseudo();
            NetworkManager.Send_Pseudo(user_self, PseudoChosen);
            Thread.sleep(1000);
        }
        return PseudoChosen;
    }

    private void Send_Pseudo(User user_self, String PseudoChosen){
        Notifications notifpseudo = new Notifications(user_self,PseudoChosen);
        ThreadManager.Send_BC(notifpseudo);
    }

    private String Ask_Pseudo(){
        System.out.println("[UserManager] Please enter a pseudo");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public void Process_Validation(Validation valid){
        if (valid.get_Valid()){
            this.Pseudo_Correct=true;
        }
    }

    public void Process_Notif_Pseudo(NotifPseudo notif, String pseudo_self) throws SocketException {
        if (ActiveUserManager.getInstance().IsinActiveListUser(notif.get_Pseudo())){
            ThreadManager.Send_BC(new Validation(notif.get_User(), pseudo_self,false));
        }
        else {
            ThreadManager.Send_BC(new Validation(notif.get_User(),pseudo_self,true));
            ActiveUserManager.getInstance().addListActiveUser(notif.get_User());
        }
    }
}
