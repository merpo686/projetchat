package Models;

import java.net.InetAddress;

public class User {
    private InetAddress IP;
    private int port;
    private String Pseudo;
    public User(InetAddress IP, int port){
        this.IP=IP;
        this.port=port;
    }
    public void Set_Pseudo(String pseudo){
        this.Pseudo=pseudo;
    }
    public String get_Pseudo(){
        return this.Pseudo;
    }
    public InetAddress get_IP(){
        return this.IP;
    }
    public int get_Port(){
        return this.port;
    }
}
