package Managers;

import Models.NotifPseudo;
import Models.Notifications;
import Models.User;
import Models.Validation;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class UserMain {
    private String Pseudo_Self = null;
    private final User user_self;

    static UserMain instance;
    private UserMain() throws UnknownHostException {
        user_self = new User(InetAddress.getLocalHost().getHostName(), 4567);
    }
    public static UserMain getInstance() throws UnknownHostException {
        if (instance == null) {
            instance = new UserMain();
        }
        return instance;
    }
    public String Get_Pseudo(){ return this.Pseudo_Self;}
    public User Get_User(){ return this.user_self;}
    public void Set_Pseudo(String pseudo){ this.Pseudo_Self=pseudo;}

    public interface NotifHandler {
        void handler(Notifications notif) throws SocketException, UnknownHostException;
    }
    public static void main(String[] args){
        try {
            ActiveUserManager AUM = ActiveUserManager.getInstance();

            NotifHandler handler = new NotifHandler() {
                @Override
                public void handler(Notifications notif) throws SocketException, UnknownHostException {
                    if (notif instanceof Validation) {
                        Validation valid = (Validation)notif;
                        NetworkManager.Process_Validation(valid);
                    }
                    else {
                        NotifPseudo notifpseudo = (NotifPseudo)notif;
                        NetworkManager.Process_Notif_Pseudo(notifpseudo);
                    }
                }
            };
            ThreadRcvBC TRBC = ThreadManager.Start_RcvThread(handler);
            UserMain.getInstance().Set_Pseudo(NetworkManager.Connection());
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
