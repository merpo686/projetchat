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
import java.text.ParseException;
import java.text.DecimalFormat;
import java.net.URL;
import java.util.ArrayList;


//button refresh to refresh displayed active users
//button for each active user with name of them on it
//action -> open chat window with user when clic
//action -> refresh display when clic

public class activelistuserInterface implements ActionListener {

    JFrame frame;
    public activelistuserInterface(){
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
        frame = new JFrame("activelistuserInterface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(120,40));
        Component contents = refreshDisplay();
        frame.getContentPane().add(contents, BorderLayout.CENTER);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    public JPanel refreshDisplay(){
        //ArrayList<User> activeusers= ActiveUserManager.getInstance().getListActiveUser();
        ArrayList<User> activeusers = new ArrayList<>();
        User merlin = new User("localhost",127);
        merlin.Set_Pseudo("merlin");
        activeusers.add(merlin);
        User merlin2 = new User("localhost",127);
        merlin.Set_Pseudo("merlin2");
        activeusers.add(merlin);
        User merlin3 = new User("localhost",127);
        merlin.Set_Pseudo("merlin3");
        activeusers.add(merlin);
        JPanel pane = new JPanel(new GridLayout(activeusers.size(),1));
        pane.setBorder(BorderFactory.createEmptyBorder(
                30, //top
                30, //left
                10, //bottom
                30) //right
        );
        JButton refreshbutton = new JButton("REFRESH");
        refreshbutton.setMnemonic(KeyEvent.VK_I);
        refreshbutton.addActionListener(this);
        pane.add(refreshbutton);
        for (User user: activeusers){
            JButton user_button = new JButton(user.get_Pseudo());
            user_button.setMnemonic(KeyEvent.VK_I);
            user_button.addActionListener(this);
            pane.add(user_button);
        }
        return pane;
    }

    public void actionPerformed(ActionEvent e) {
        // Using getActionCommand() method is a bit of a hack, but for the
        // sake of this exercise, it serves its purpose.
        if (e.getActionCommand().equals("REFRESH")){
            frame.getContentPane().removeAll();
            frame.getContentPane().invalidate();
            frame.getContentPane().add(refreshDisplay());
            frame.getContentPane().revalidate();
        }
        else{

        }
    }

    private static void initLookAndFeel() {

        // Swing allows you to specify which look and feel your program uses--Java,
        // GTK+, Windows, and so on as shown below.
        String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (ClassNotFoundException e) {
            System.err.println("Couldn't find class for specified look and feel:"
                    + lookAndFeel);
            System.err.println("Did you include the L&F library in the class path?");
            System.err.println("Using the default look and feel.");
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Can't use the specified look and feel ("
                    + lookAndFeel
                    + ") on this platform.");
            System.err.println("Using the default look and feel.");
        } catch (Exception e) {
            System.err.println("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason.");
            System.err.println("Using the default look and feel.");
            e.printStackTrace();
        }
    }

}

