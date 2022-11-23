package Managers;

import Models.*;

public class NetworkManager {
    String Pseudo;
    UserManager UM;
    public NetworkManager(String Pseudo, UserManager UM){
        this.Pseudo=Pseudo;
        this.UM=UM;
    }
    public void Receive_BC(Notif notif){
        if (ActiveUserManager.IsinActiveListUser(notif.get_Pseudo())){
            ThreadSendBC.send(new Validation(2,this.Pseudo,false));
        }
        else {
            ThreadSendBC.send(new Validation(2,this.Pseudo,true));
        }
    }
    public void Receive_BC(Validation valid){
        UM.Process_Pseudo_Response(valid);
    }
}
