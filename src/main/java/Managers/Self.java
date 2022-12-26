package Managers;

import Models.*;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Self {


    private String pseudo_Self = null;
    private final User user_self;
    static Self instance;
    private int port = 4567;

    private Self() throws UnknownHostException {
        user_self = new User(InetAddress.getLocalHost().getHostName(), get_Port());
    }

    public static Self getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new Self();
        }
        return instance;
    }

    public String get_Pseudo(){ return this.pseudo_Self;}
    public User get_User(){ return this.user_self;}
    public int get_Port(){return this.port;}
    public void set_Port(int port){this.port=port;}
    public void set_User(String Pseudo){this.user_self.Set_Pseudo(Pseudo);}
    public void set_Pseudo(String pseudo){
        this.pseudo_Self =pseudo;
        this.set_User(this.get_Pseudo());
        ActiveUserManager.getInstance().addListActiveUser(this.get_User());
    }

}
