package Models;

import Managers.*;

public class Connection extends Notifications{
    private final Boolean connect;
    static Connection instance;
    public Connection( User user, boolean connect){
        super(user);
        this.connect=connect;
    }

    public boolean get_Valid(){
        return this.connect;
    }
    public String toString(){return this.connect.toString();}
}
