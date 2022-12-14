package Graphics;

/*
 * SwingApplication.java is a 1.4 example that requires
 * no other files.
 */
import Managers.ActiveUserManager;
import Managers.NetworkManager;
import Managers.UserMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.text.NumberFormatter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.DecimalFormat;
import java.net.URL;

public class ChoosePseudoInterface implements ActionListener {
    private static JFrame frame;
    final JLabel label = new JLabel("Pseudo already taken");
    JFormattedTextField connection;
    JPanel pane;

    public ChoosePseudoInterface(){
        //Set the look and feel.
        String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            System.err.println("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason.");
            System.err.println("Using the default look and feel.");
            e.printStackTrace();
        }
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        frame = new JFrame("ChatApplication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(120,40));
        Component contents = createComponents();
        frame.getContentPane().add(contents, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public Component createComponents() {
        Action connection_button = new AbstractAction("CONNECTION"){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String PseudoChosen= connection.getText();
                if (ActiveUserManager.getInstance().IsinActiveListUser(PseudoChosen)){
                    pane.add(label);
                    frame.repaint();
                }
                else {
                    try {
                        UserMain.getInstance().Set_Pseudo(PseudoChosen);
                        NetworkManager.Send_Pseudo(PseudoChosen);
                    } catch (UnknownHostException | SocketException unknownHostException) {
                        unknownHostException.printStackTrace();
                    }
                    frame.setVisible(false);
                    new activelistuserInterface();
                    frame.dispose();
                }
            }
        };
        connection_button.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_ENTER);
        // https://coderanch.com/t/346109/java/actions-performed

        connection = new JFormattedTextField();
        connection.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        //Set and commit the default temperature.
        try {
            connection.setText("Enter Pseudo");
            connection.commitEdit();
        } catch(ParseException e) {
            //Shouldn't get here unless the setText value doesn't agree
            //with the format set above.
            e.printStackTrace();
        }
        /*
         * An easy way to put space between a top-level container
         * and its contents is to put the contents in a JPanel
         * that has an "empty" border.
         */
        pane = new JPanel(new GridLayout(2,2));
        pane.add(connection);
        pane.add(connection_button);
        pane.setBorder(BorderFactory.createEmptyBorder(
                30, //top
                30, //left
                10, //bottom
                30) //right
        );
        return pane;
    }

    public void actionPerformed(ActionEvent e) {
        // Using getActionCommand() method is a bit of a hack, but for the
        // sake of this exercise, it serves its purpose.
        if (e.getActionCommand().equals("CONNECTION")||e.getActionCommand().equals()){
            //choose pseudo and send it to others; connection.getText();
            //either pseudo is correct or wrong if wrong -> set label visible pane.add(label);
            //if true -> pass to other window

        }
    }
}
