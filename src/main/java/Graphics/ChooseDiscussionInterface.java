package Graphics;
import Managers.*;
import Models.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ChooseDiscussionInterface extends Container {
    JFrame frame;

    //menu actions
    Action refresh_button = new AbstractAction("REFRESH") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            removeAll();
            invalidate();
            try {
                refreshDisplay();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
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

    public ChooseDiscussionInterface(JFrame frame) throws UnknownHostException {
        this.frame=frame;
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.set_state("ChooseDiscussionInterface");
        IM.set_user(null);
        refreshDisplay();
    }

    public void refreshDisplay() throws UnknownHostException {
        ArrayList<User> activeusers= ActiveUserManager.getInstance().getListActiveUser();
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

