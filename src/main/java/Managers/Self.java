package Managers;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Self {
    private String pseudo_Self = null;
    static Self instance;
    static final int portTCP=12341;
    static final int portUDP=12340;
    private final String hostname;
    private Self() throws UnknownHostException {
        hostname = InetAddress.getLocalHost().getHostName();
    }
    public static Self getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new Self();
        }
        return instance;
    }
    public String getHostname(){return this.hostname;}
    public String get_Pseudo(){ return this.pseudo_Self;}
    public void set_Pseudo(String pseudo){
        this.pseudo_Self =pseudo;
    }
}
