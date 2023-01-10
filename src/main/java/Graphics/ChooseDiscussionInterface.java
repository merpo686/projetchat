package Graphics;
import Managers.*;
import Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**Interface on which the user chooses who to chat with, refreshable*/
public class ChooseDiscussionInterface extends Container {
    JFrame frame;

    //menu actions
    Action refreshButton = new AbstractAction("REFRESH") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            removeAll();
            invalidate();
            refreshDisplay();
            revalidate();
        }
    };
    Action changePseudoButton = new AbstractAction("CHANGE PSEUDO") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            new ChoosePseudoInterface(frame);
        }
    };
    Action disconnectionButton = new AbstractAction("DECONNEXION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            NetworkManager.SendDisconnection();
            frame.dispose();
        }
    };
    /**Constructor */
    public ChooseDiscussionInterface(JFrame frame) {
        this.frame=frame;
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.setState("ChooseDiscussionInterface");
        IM.setUser(null);
        refreshDisplay();
    }

    /**The core of the interface is defined here, to make it refreshable*/
    public void refreshDisplay(){
        ArrayList<User> activeusers= ActiveUserManager.getInstance().getListActiveUser();
        System.out.println(ActiveUserManager.getInstance().toString());
        //gridlayout, maybe not the best
        setLayout(  new GridLayout(activeusers.size(),1));
        //creates buttons for each users
        for (User user: activeusers){
            Action userButton = new AbstractAction(user.getPseudo()) {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    setVisible(false);
                    new ChatInterface(frame,user);
                }
            };
            add(new JButton(userButton));
        }
        //create menu
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Menu");
        file.add(refreshButton);
        file.add(changePseudoButton);
        file.add(disconnectionButton);
        bar.add(file);
        frame.setJMenuBar(bar);
        frame.setContentPane(this);
    }

}

