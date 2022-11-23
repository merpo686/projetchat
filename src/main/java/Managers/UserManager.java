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
    public UserManager() throws InterruptedException,UnknownHostException,SocketException{
        user_self= new User(InetAddress.getLocalHost(),4567);
        boolean pseudo_Incorrect = true;
        responsePseudo=true;
        NetworkManager NM = new NetworkManager(this);
        ThreadManager TM = new ThreadManager(NM);
        while (pseudo_Incorrect) {
            this.Connect();
<<<<<<< HEAD
=======
            System.out.println("Sending Pseudos to others");
<<<<<<< HEAD
            Thread.sleep(5000);
=======
>>>>>>> 04c7077aeb476914f91fdfe66e930ee28dca301e
            Thread.sleep(1);
>>>>>>> 73086359ee7cd70752715e3494db00c68c7e3a50
            if (responsePseudo){
                System.out.println("no response, or pseudo ok");
                pseudo_Incorrect =false;
                user_self.Set_Pseudo(Pseudochoosed);
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
