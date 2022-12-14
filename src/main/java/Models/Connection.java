package Models;

import Managers.*;

public class Connection extends Notifications{
    private Boolean connect;
    static Connection instance;
    public Connection( User user, boolean connect){
        super(user);
        this.connect=connect;
    }

    /*public static Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }*/

    public boolean get_Valid(){
        return this.connect;
    }
    public String toString(){return this.connect.toString();}
}
