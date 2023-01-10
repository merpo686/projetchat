package Models;

public class User {
    private final String hostname;
    private final String Pseudo;
    public User(String hostname,String Pseudo){
        this.hostname=hostname;
        this.Pseudo=Pseudo;
    }
    public String getPseudo(){
        return this.Pseudo;
    }
    public String getHostname(){
        return this.hostname;
    }
    public Boolean equals(String hostname){return this.getHostname().equals(hostname);}
    public String toString(){return this.getPseudo();}
}