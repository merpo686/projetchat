package Models;

public class User {
    private final String hostname;
    private final int portTCP;
    private final int portUDP;
    private String Pseudo;
    public User(String hostname, int portTCP,int portUDP){
        this.hostname=hostname;
        this.portTCP =portTCP;
        this.portUDP =portUDP;
    }
    public User(String hostname){
        this.hostname=hostname;
        this.portTCP =-1;
        this.portUDP=-1;
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
    public int get_PortTCP(){
        return this.portTCP;
    }
    public int getPortUDP(){return this.portUDP;}
    public Boolean equals(User other){return this.get_Hostname().equals(other.get_Hostname());}
    public Boolean equals(String hostname){return this.get_Hostname().equals(hostname);}
}