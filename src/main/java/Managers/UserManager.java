package Managers;

import Models.*;
import java.net.*;
import java.io.*;
import Managers.*;

import javax.management.Notification;
import java.util.Scanner;

public class UserManager {
    public static User user_self;
    private String Pseudochoosed;
    private Boolean responsePseudo;
    public static ActiveUserManager AUM = new ActiveUserManager();
    public UserManager() throws InterruptedException,UnknownHostException,SocketException{
        user_self= new User(InetAddress.getLocalHost().getHostName(), 4567);
        boolean pseudo_Incorrect = true;
        responsePseudo=true;
        NetworkManager NM = new NetworkManager(this);
        ThreadManager TM = new ThreadManager(NM);
        System.out.println(UserManager.user_self.get_Hostname());
        while (pseudo_Incorrect) {
            this.Connect();
            System.out.println("[UserManager] Sending Pseudos to others");
            Thread.sleep(5000);
            if (responsePseudo){
                System.out.println("[UserManager] no response, or pseudo ok");
                pseudo_Incorrect =false;
                user_self.Set_Pseudo(Pseudochoosed);
                ActiveUserManager.addListActiveUser(user_self);
            }
        }
    }
    public void Connect() throws UnknownHostException,SocketException{
        String Pseudochoosed=Ask_Pseudo();
        Notifications notifpseudo = new Notifications(user_self,Pseudochoosed);
        ThreadManager.Send_BC(notifpseudo);
    }
    private String Ask_Pseudo(){
        System.out.println("[UserManager] Please enter a pseudo");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    public void Process_Pseudo_Response(Validation valid){
        if (valid.get_Valid()){
            System.out.println("C'est pas valide");
            this.responsePseudo=false;
        }
    }
}
