package Graphics;

import Managers.ActiveUserManager;
import Managers.NetworkManager;
import Managers.Self;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;

//when change pseudo doit changer les pseudos actifs!!!

/** Implements the interface on which the user chooses his pseudo */
public class ChoosePseudoInterface extends Container {

    private static final Logger LOGGER = LogManager.getLogger(ChoosePseudoInterface.class);
    JFormattedTextField connection;
    GridBagConstraints cons;
    JFrame frame;

    //action of the menu
    //deconnexion button in the menu
    Action disconnectionButton = new AbstractAction("DECONNEXION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            NetworkManager.SendDisconnection();
            frame.dispose();
        }
    };
    /**Constructor */
    public ChoosePseudoInterface(JFrame frame){
        this.frame=frame;
        InterfaceManager IM=InterfaceManager.getInstance();
        IM.setState("ChoosePseudoInterface");
        IM.setUser(null);
        //GridBagLayout, maybe not the best either but I found it smooth
        setLayout(new GridBagLayout());
        cons= new GridBagConstraints();

        //connection button
        Action connectionButton = new AbstractAction("CONNECTION"){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    connection.commitEdit();
                } catch (ParseException e) {
                    LOGGER.error("Failed to edit the connection button with chosen pseudo.");
                    e.printStackTrace();
                }
                String PseudoChosen= connection.getText();
                if (ActiveUserManager.getInstance().IsinActiveListUser(PseudoChosen)){
                    add(new JLabel("Pseudo already taken: "+PseudoChosen));
                    repaint();
                }
                else {
                    Self.getInstance().setPseudo(PseudoChosen);
                    NetworkManager.SendPseudo();
                    setVisible(false);
                    new ChooseDiscussionInterface(frame);
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
        add(new JButton(connectionButton),cons);

        //input text bar: the same as connection button but when pressing enter
        connection = new JFormattedTextField();
        connection.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        connection.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_ENTER){
                    try {
                        connection.commitEdit();
                    } catch (ParseException exc) {
                        LOGGER.error("Failed to edit the connection button with chosen pseudo.");
                        exc.printStackTrace();
                    }
                    String PseudoChosen= connection.getText();
                    if (ActiveUserManager.getInstance().IsinActiveListUser(PseudoChosen)){
                        add(new JLabel("Pseudo already taken: "+PseudoChosen));
                        repaint();
                    }
                    else {
                        Self.getInstance().setPseudo(PseudoChosen);
                        NetworkManager.SendPseudo();
                        setVisible(false);
                        new ChooseDiscussionInterface(frame);
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
        file.add(disconnectionButton); // adds a menu item called disconnectionButton
        bar.add(file);
        frame.setJMenuBar(bar);
        frame.setContentPane(this);
    }
}
