package Graphics;

import Managers.Self;
import Models.User;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class InterfaceManager {
    public static Color background_color = new Color(15,5, 107);
    public static Color foreground_color = new Color(252,210,28);
    String state;
    User user_discussion;
    static InterfaceManager instance;
    private InterfaceManager() {
         user_discussion=null;
         state=null;
    }
    public String get_state(){return state;}
    public User get_user(){return user_discussion;}
    public void set_state(String state){this.state=state;}
    public void set_user(User user){this.user_discussion=user;}
    public static InterfaceManager getInstance() {
        if (instance == null) {
            instance = new InterfaceManager();
        }
        return instance;
    }
}
