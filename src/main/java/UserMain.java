import Managers.ActiveUserManager;
import Managers.NetworkManager;
import Managers.ThreadManager;
import Managers.ThreadRcvBC;
import Managers.UserManager;
import Models.Notifications;
import Models.User;
import Models.Validation;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UserMain {

    interface NotifHandler {
        void handler(Notifications notif);
    }


    public static void main(String[] args){
        try {
            ActiveUserManager AUM = ActiveUserManager.getInstance();
            User user_self= new User(InetAddress.getLocalHost().getHostName(), 4567);

            NetworkManager NM= new NetworkManager();
            NotifHandler handler = new NotifHandler() {
                @Override
                public void handler(Notifications notif) {
                    if (notif instanceof Validation) {
                        NM.Process_Validation(notif);
                    }
                    else {
                        NM.Process_Notif_Pseudo(notif);
                    }
                }
            };
            ThreadRcvBC TRBC = ThreadManager.Start_RcvThread(handler);

            NetworkManager.Connection(user_self);
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
