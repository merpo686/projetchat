package Models;
import Models.*;

import java.net.InetAddress;

public class Notifications {
    private final User user;
    public Notifications(User user,String Pseudo){
        this.user= user;
        this.user.Set_Pseudo(Pseudo);
    }
    public String get_Pseudo(){return this.user.get_Pseudo();}
    public User get_User(){return this.user;}
    public int get_numPort(){return this.user.get_Port();}
    public InetAddress get_IP(){return this.user.get_IP();}
}
