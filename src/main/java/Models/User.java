package Models;

public class User {
    private final String hostname;
    private final String Pseudo;
    public User(String hostname,String Pseudo){
        this.hostname=hostname;
        this.Pseudo=Pseudo;
    }
    public String get_Pseudo(){
        return this.Pseudo;
    }
    public String get_Hostname(){
        return this.hostname;
    }
    public Boolean equals(String hostname){return this.get_Hostname().equals(hostname);}
}