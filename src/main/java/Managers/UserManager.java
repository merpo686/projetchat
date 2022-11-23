package Managers;

import Models.*;
import Managers.*;

import java.util.Scanner;

public class UserManager {
    private String Pseudo;
    private NetworkManager NM;
    private ThreadManager TM;
    private String Pseudochoosed;
    public UserManager(String Pseudo){
        this.Pseudo="";
        this.NM=new NetworkManager(this.Pseudo,this);
        this.TM=new ThreadManager(this.NM);
        this.Connect();
    }
    public void Connect(){
        String Pseudochoosed=Ask_Pseudo();
        Notif notifpseudo = new Notif(3,Pseudochoosed); //type3 = notif sert à r pour l'instant
        ThreadManager.Send_BC(notifpseudo);
    }
    private String Ask_Pseudo(){
        Scanner sc = new Scanner(System.in);
        String ps = sc.nextLine();
        return ps;
    }
    public void Process_Pseudo_Response(Validation valid){
        if (valid.get_Valid()){
            this.Pseudo=this.Pseudochoosed;
        }
        else {
           this.Connect();
        }
    }
}
