package Managers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Represents all infos we have on ourselves
 */
public class Self {
    private static final Logger LOGGER = LogManager.getLogger(Self.class);
    private String pseudoSelf = null;
    static Self instance;
    static final int portTCP=12341;
    static final int portUDP=12340;
    private final String hostname;

    /**
     * Constructor
     * @throws UnknownHostException
     */
    private Self() throws UnknownHostException {
        hostname = InetAddress.getLocalHost().getHostName();
    }

    /**
     * @return instance of Self
     */
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

    /**
     * @return our hostname
     */
    public String getHostname(){return this.hostname;}

    /**
     * @return our pseudo
     */
    public String getPseudo(){ return this.pseudoSelf;}

    /**
     * To modify our pseudo
     * @param pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudoSelf =pseudo;
    }
}
