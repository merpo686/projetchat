package Models;

public class Connection{
    private final Boolean connect;
    private final String hostname;
    public Connection( String hostname, boolean connect){
        this.connect=connect;
        this.hostname=hostname;
    }
    public boolean getValid(){
        return this.connect;
    }
    public String getHostname(){return this.hostname;}
}
