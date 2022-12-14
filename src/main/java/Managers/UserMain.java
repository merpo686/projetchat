package Managers;

import Graphics.ChoosePseudoInterface;
import Models.*;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.lang.Thread;


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
    public void Set_User(String Pseudo){this.user_self.Set_Pseudo(Pseudo);}
    public void Set_Pseudo(String pseudo){
        this.Pseudo_Self=pseudo;
        this.Set_User(this.Get_Pseudo());
        ActiveUserManager.getInstance().addListActiveUser(this.Get_User());
    }

    public interface NotifHandler {
        void handler(Notifications notif) throws SocketException, UnknownHostException;
    }
    public static void main(String[] args){
        try {
            ActiveUserManager AUM = ActiveUserManager.getInstance();

            NotifHandler handler = new NotifHandler() {
                @Override
                public void handler(Notifications notif) throws SocketException, UnknownHostException {
                    if (notif instanceof Connection) {
                        Connection connect =(Connection) notif;
                        NetworkManager.Process_Connection(connect);
                    }
                    else if (notif instanceof NotifPseudo) {
                        NotifPseudo notifpseudo = (NotifPseudo)notif;
                        NetworkManager.Process_Notif_Pseudo(notifpseudo);
                    }
                    else if (notif instanceof Message) {

                    }
                }
            };
            //test Set_Pseudo / Get_Pseudo
            //System.out.println(UserMain.getInstance().Get_Pseudo());
            //UserMain.getInstance().Set_Pseudo(NetworkManager.Ask_Pseudo());
            //System.out.println(UserMain.getInstance().Get_Pseudo());
            //test RCV notif
            ThreadRcvBC TRBC = ThreadManager.Start_RcvThread(handler);
            //connection
            NetworkManager.Send_Connection();
            Thread.sleep(1000);
            System.out.println("Pseudo choisis: "+ActiveUserManager.getInstance().toString());
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ChoosePseudoInterface();
                }
            });
            /*String PseudoChosen=NetworkManager.Ask_Pseudo();
            while (ActiveUserManager.getInstance().IsinActiveListUser(PseudoChosen)){
                System.out.println("Pseudo déjà choisi");
                PseudoChosen=NetworkManager.Ask_Pseudo();
            }
            UserMain.getInstance().Set_Pseudo(PseudoChosen);
            NetworkManager.Send_Pseudo(PseudoChosen);
            Thread.sleep(5000);
            System.out.println("Disconnecting");
            NetworkManager.Send_Disconnection();*/
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
