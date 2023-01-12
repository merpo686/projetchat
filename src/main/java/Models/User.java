package Models;

public class User {
    private final String hostname;
    private final String Pseudo;

    /**Constructor
     *
     * @param hostname
     * @param Pseudo
     */
    public User(String hostname,String Pseudo){
        this.hostname=hostname;
        this.Pseudo=Pseudo;
    }

    /**
     *
     * @return Pseudo
     */
    public String getPseudo(){
        return this.Pseudo;
    }

    /**
     *
     * @return Hostname
     */
    public String getHostname(){
        return this.hostname;
    }

    /**
     * Check if the hostname is the same
     * @param hostname
     * @return true if equals
     */
    public Boolean equals(String hostname){return this.getHostname().equals(hostname);}

    /**
     *
     * @return String value of User (pseudo and hostname)
     */
    public String toString(){return "User: "+this.getPseudo()+" Hostname:"+this.getHostname();}
}