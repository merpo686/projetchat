package Models;

public class User {
    private final String hostname;
    private final int port;
    private String Pseudo;
    public User(String hostname, int port){
        this.hostname=hostname;
        this.port=port;
    }
    public void Set_Pseudo(String pseudo){
        this.Pseudo=pseudo;
    }
    public String get_Pseudo(){
        return this.Pseudo;
    }
    public String get_Hostname(){
        return this.hostname;
    }
    public int get_Port(){
        return this.port;
    }
}
