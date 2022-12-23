package Graphics;
/*
 * SwingApplication.java is a 1.4 example that requires
 * no other files.
 */
import Managers.*;
import Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.NumberFormatter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.net.URL;
import java.util.ArrayList;


//link to list of activ users in refresh (remove the manual test list)

public class ChooseDiscussionInterface extends Container {
    JFrame frame;

    //menu actions
    Action refresh_button = new AbstractAction("REFRESH") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            removeAll();
            invalidate();
            refreshDisplay();
            revalidate();
        }
    };

    Action change_pseudo_button = new AbstractAction("CHANGE PSEUDO") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            new ChoosePseudoInterface(frame);
        }
    };

    Action deconnexion_button = new AbstractAction("DECONNEXION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            try {
                NetworkManager.Send_Disconnection();
            } catch (SocketException | UnknownHostException e) {
                e.printStackTrace();
            }
            frame.dispose();
        }
    };

    public ChooseDiscussionInterface(JFrame frame){
        this.frame=frame;
        refreshDisplay();
    }

    public void refreshDisplay(){
        //ArrayList<User> activeusers= ActiveUserManager.getInstance().getListActiveUser();
        ArrayList<User> activeusers = new ArrayList<>();
        User merlin = new User("localhost",127);
        merlin.Set_Pseudo("merlin");
        activeusers.add(merlin);
        User merlin2 = new User("localhost",127);
        merlin2.Set_Pseudo("merlin2");
        activeusers.add(merlin2);
        User merlin3 = new User("localhost",127);
        merlin3.Set_Pseudo("merlin3");
        activeusers.add(merlin3);

        //gridlayout, maybe not the best
        setLayout(  new GridLayout(activeusers.size(),1));

        //creates buttons for each users
        for (User user: activeusers){
            Action user_button = new AbstractAction(user.get_Pseudo()) {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    setVisible(false);
                    new ChatInterface(frame,user);
                }
            };
            add(new JButton(user_button));
        }

        //create menu
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Menu");
        file.add(refresh_button);
        file.add(change_pseudo_button);
        file.add(deconnexion_button);
        bar.add(file);
        frame.setJMenuBar(bar);
        frame.setContentPane(this);
    }

}

