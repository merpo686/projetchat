package Managers;

import Models.*;
import java.net.*;
import Managers.*;

import javax.management.Notification;
import java.util.Scanner;

public class UserManager {
    public static User user_self;
    private String Pseudochoosed;
    private Boolean responsePseudo;
    public UserManager() throws InterruptedException,UnknownHostException{
        user_self= new User(InetAddress.getLocalHost(),4567);
        boolean pseudo_Incorrect = true;
        responsePseudo=true;
        NetworkManager NM = new NetworkManager(this);
        ThreadManager TM = new ThreadManager(NM);
        while (pseudo_Incorrect) {
            this.Connect();
            wait(1);
            if (responsePseudo){
                pseudo_Incorrect =false;
                user_self.Set_Pseudo(Pseudochoosed);
            }
        }
    }
    public void Connect() throws UnknownHostException{
        String Pseudochoosed=Ask_Pseudo();
        Notifications notifpseudo = new Notifications(user_self,Pseudochoosed);
        ThreadManager.Send_BC(notifpseudo);
    }
    private String Ask_Pseudo(){
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    public void Process_Pseudo_Response(Validation valid){
        if (!(valid.get_Valid())){
            this.responsePseudo=false;
        }
    }
}
