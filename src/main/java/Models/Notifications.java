package Models;
import Models.*;
public class Notifications {
    private User user;
    public Notifications(User user,String Pseudo){
        this.user= user;
        this.user.Set_Pseudo(Pseudo);
    }
    public String get_Pseudo(){return this.user.get_Pseudo();}
    public User get_User(){return this.user;}
}
