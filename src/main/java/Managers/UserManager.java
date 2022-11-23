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
        user_self= new User(InetAddress.getLocalHost(),4567);
        boolean pseudo_Incorrect = true;
        responsePseudo=true;
        NetworkManager NM = new NetworkManager(this);
        ThreadManager TM = new ThreadManager(NM);
        while (pseudo_Incorrect) {
            this.Connect();
            System.out.println("Sending Pseudos to others");
            Thread.sleep(5000);
            if (responsePseudo){
                System.out.println("no response, or pseudo ok");
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
        System.out.println("Please enter a pseudo");
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    public void Process_Pseudo_Response(Validation valid){
        if (!(valid.get_Valid())){
            this.responsePseudo=false;
        }
    }
}
