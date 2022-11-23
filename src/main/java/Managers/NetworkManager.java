package Managers;

import Models.*;

public class NetworkManager {
    UserManager UM;
    public NetworkManager( UserManager UM){
        this.UM=UM;
    }
    public void Receive_BC(Notifications notif){
        if (ActiveUserManager.IsinActiveListUser(notif.get_Pseudo())){
            ThreadSendBC.send(new Validation(UserManager.user_self,false));
        }
        else {
            ThreadSendBC.send(new Validation(UserManager.user_self,true));
            ActiveUserManager.addListActiveUser(notif.get_User());
        }
    }
    public void Receive_BC(Validation valid){
        UM.Process_Pseudo_Response(valid);
    }
}
