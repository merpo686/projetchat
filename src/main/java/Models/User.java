package Models;

public class User {
    private String IP;
    private int port;
    private String Pseudo;
    public User(String IP, int port){
        this.IP=IP;
        this.port=port;
    }
    public void Set_Pseudo(String pseudo){
        this.Pseudo=pseudo;
    }
    public String get_Pseudo(){
        return this.Pseudo;
    }
    public String get_IP(){
        return this.IP;
    }
    public int get_Port(){
        return this.port;
    }
}
