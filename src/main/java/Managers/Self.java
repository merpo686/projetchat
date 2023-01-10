package Managers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class Self {
    private static final Logger LOGGER = LogManager.getLogger(Self.class);
    private String pseudo_Self = null;
    static Self instance;
    static final int portTCP=12341;
    static final int portUDP=12340;
    private final String hostname;
    private Self() throws UnknownHostException {
        hostname = InetAddress.getLocalHost().getHostName();
    }
    public static Self getInstance() {
        if (instance == null) {
            try {
                instance = new Self();
            }
            catch (UnknownHostException e){
                LOGGER.error("Unable to initialize Self class : unable to get Hostname.");
                e.printStackTrace();
                System.exit(1);
            }
        }
        return instance;
    }
    public String getHostname(){return this.hostname;}
    public String get_Pseudo(){ return this.pseudo_Self;}
    public void set_Pseudo(String pseudo) {
        this.pseudo_Self =pseudo;
    }
}
