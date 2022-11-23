package Managers;
import Managers.*;
import Models.*;

public class NetworkManager {
    UserManager UM;
    public NetworkManager( UserManager UM){
        this.UM=UM;
    }
    public void Receive_BC(Notifications notif){
        if (ActiveUserManager.IsinActiveListUser(notif.get_Pseudo())){
            ThreadManager.Send_BC(new Validation(UserManager.user_self, UserManager.user_self.get_Pseudo(),false));
        }
        else {
            ThreadManager.Send_BC(new Validation(UserManager.user_self,UserManager.user_self.get_Pseudo(),true));
            ActiveUserManager.addListActiveUser(notif.get_User());
        }
    }
    public void Receive_BC(Validation valid){
        UM.Process_Pseudo_Response(valid);
    }
}
