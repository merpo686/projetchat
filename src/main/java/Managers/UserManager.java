package Managers;

import Models.*;

import java.util.Scanner;

public class UserManager {
    String Pseudo;
    NetworkManager NM;
    ThreadManager TM;
    public UserManager(String Pseudo){
        this.Pseudo=Pseudo;
        this.NM=new NetworkManager(5000);
    }
    public void Connect(){
        String pseudochoosed=Ask_Pseudo();
        Notif notifpseudo = new Notif(3,pseudochoosed); //type3 = notif sert à r pour l'instant
        ThreadManager.Send_BC(notifpseudo);
    }
    private String Ask_Pseudo(){
        Scanner sc = new Scanner(System.in);
        String ps = sc.nextLine();
        return ps;
    }
}
