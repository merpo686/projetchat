package Graphics;

import Managers.ActiveUserManager;
import Managers.NetworkManager;
import Managers.Self;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class ChoosePseudoInterface extends Container {
    JFormattedTextField connection;
    GridBagConstraints cons;
    JFrame frame;

    //action of the menu
    //deconnexion button in the menu
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

    public ChoosePseudoInterface(JFrame frame){
        this.frame=frame;
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.set_state("ChoosePseudoInterface");
        IM.set_user(null);
        //GridBagLayout, maybe not the best either but I found it smooth
        setLayout(new GridBagLayout());
        cons= new GridBagConstraints();

        //connection button
        Action connection_button = new AbstractAction("CONNECTION"){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    connection.commitEdit();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String PseudoChosen= connection.getText();
                if (ActiveUserManager.getInstance().IsinActiveListUser(PseudoChosen)){
                    add(new JLabel("Pseudo already taken: "+PseudoChosen));
                    repaint();
                }
                else {
                    try {
                        ActiveUserManager.getInstance().removeListActiveUser(Self.getInstance().get_User());
                        Self.getInstance().set_Pseudo(PseudoChosen);
                        NetworkManager.Send_Pseudo(PseudoChosen);
                    } catch (UnknownHostException | SocketException unknownHostException) {
                        unknownHostException.printStackTrace();
                    }
                    setVisible(false);
                    try {
                        new ChooseDiscussionInterface(frame);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //positioning
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weighty=1.0;
        cons.gridwidth=2;
        cons.ipadx=10;
        cons.ipady=10;
        cons.gridx=1;
        cons.gridy=2;
        add(new JButton(connection_button),cons);

        //input text bar
        connection = new JFormattedTextField();
        connection.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        connection.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    try {
                        connection.commitEdit();
                    } catch (ParseException exc) {
                        exc.printStackTrace();
                    }
                    String PseudoChosen= connection.getText();
                    if (ActiveUserManager.getInstance().IsinActiveListUser(PseudoChosen)){
                        add(new JLabel("Pseudo already taken: "+PseudoChosen));
                        repaint();
                    }
                    else {
                        try {
                            ActiveUserManager.getInstance().removeListActiveUser(Self.getInstance().get_User());
                            Self.getInstance().set_Pseudo(PseudoChosen);
                            NetworkManager.Send_Pseudo(PseudoChosen);
                        } catch (UnknownHostException | SocketException unknownHostException) {
                            unknownHostException.printStackTrace();
                        }
                        setVisible(false);
                        try {
                            new ChooseDiscussionInterface(frame);
                        } catch (UnknownHostException unknownHostException) {
                            unknownHostException.printStackTrace();
                        }
                    }
                }
            }
        });
        //positioning
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weighty=1.0;
        cons.gridwidth=2;
        cons.gridx=1;
        cons.gridy=1;
        add(connection,cons);

        //menu
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("Menu");
        file.add(deconnexion_button); // adds a menu item called deconnexion_button
        bar.add(file);
        frame.setJMenuBar(bar);
        frame.setContentPane(this);
    }
}
