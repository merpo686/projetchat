package Models;

/**Class used for connection purposes: send true when connecting, false when disconnecting
 */
public class Connection{
    private final Boolean connect;
    private final String hostname;

    /**
     * Constructor
     * @param hostname
     * @param connect
     */
    public Connection( String hostname, boolean connect){
        this.connect=connect;
        this.hostname=hostname;
    }

    /**
     * @return boolean connect
     */
    public boolean getValid(){
        return this.connect;
    }

    /**
     * @return hostname
     */
    public String getHostname(){return this.hostname;}
}
