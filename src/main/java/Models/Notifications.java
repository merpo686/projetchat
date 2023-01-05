package Models;
import Models.*;

import java.net.InetAddress;

public class Notifications {
    private final User user;
    public Notifications(User user){
        this.user= user;
    }
    public User get_User(){return this.user;}
    public int get_numPort(){return this.user.getPortUDP();}
    public String get_Hostname(){return this.user.get_Hostname();}
}
