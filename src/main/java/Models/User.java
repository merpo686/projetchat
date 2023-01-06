package Models;

public class User {
    private final String hostname;
    private String Pseudo = null;
    public User(String hostname,String Pseudo){
        this.hostname=hostname;
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
    public Boolean equals(String hostname){return this.get_Hostname().equals(hostname);}
}