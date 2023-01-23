package Graphics;

import ActivityManagers.ActiveUserManager;
import ActivityManagers.Self;
import Conversations.ConversationsManager;
import Conversations.MessageAccessProblem;
import Models.*;
import Threads.TCPClientHandler;
import Threads.ThreadManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Objects;

public class Interface extends JFrame {
    private static final Color backgroundColor = new Color(15,5, 107); //colors by default of the interface
    private static final Color foregroundColor = new Color(252,210,28);
    private static final Logger LOGGER = LogManager.getLogger(Interface.class);
    private final JFrame frame;
    /**Actions independents from their container, which will be put in the menu bar*/
    Action changeDiscussionButton = new AbstractAction("CHANGE DISCUSSION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new ChooseDiscussionInterface(frame);
        }
    };
    Action changePseudoButton = new AbstractAction("CHANGE PSEUDO") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new ChoosePseudoInterface(frame);
        }
    };
    Action disconnectionButton = new AbstractAction("DISCONNECTION") {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
            ThreadManager.SendDisconnection();
            dispose();
        }
    };
    /** Sets up the original frame, and start the first interface*/
    public Interface(){
        super();
        LOGGER.debug("Launching interface");
        //Set the look and feel.
        String lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            LOGGER.error("Couldn't get specified look and feel ("
                    + lookAndFeel
                    + "), for some reason. Using the default look and feel.");
            e.printStackTrace();
        }
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                ThreadManager.SendDisconnection();
                dispose();
            }
        });
        //Display the window.
        setMinimumSize(new Dimension(400,300));
        setForeground(foregroundColor);
        setBackground(backgroundColor);
        setLocationRelativeTo(null); //center of the screen
        frame=this;
        new ChoosePseudoInterface(this); //first interface displayed
        setVisible(true);
    }
    /** Implements the interface on which the user chooses his pseudo */
    public class ChoosePseudoInterface extends Container {
        JFormattedTextField connection;
        GridBagConstraints cons;
        JFrame frame;

        //connection
        public void connectionEvent(){
            try {
                connection.commitEdit();
            } catch (ParseException e) {
                LOGGER.error("Failed to edit the connection button with chosen pseudo.");
                e.printStackTrace();
            }
            String pseudoChosen = connection.getText();
            if (pseudoChosen.equals("")||pseudoChosen.equals("true")||pseudoChosen.equals("false")||pseudoChosen.length()>20){
                JOptionPane.showMessageDialog(frame, "Pseudo can't be null, true/false or longer then 20 characters",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else if (ActiveUserManager.getInstance().IsinActiveListUser(pseudoChosen)){
                JOptionPane.showMessageDialog(frame, "Pseudo taken "+pseudoChosen,
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
            }
            else {
                Self.getInstance().setPseudo(pseudoChosen);
                ThreadManager.SendPseudo();
                setVisible(false);
                new ChooseDiscussionInterface(frame);
            }
        }

        /**Constructor
         * @param frame in activity
         * */
        public ChoosePseudoInterface(JFrame frame){
            this.frame=frame;
            //GridBagLayout, maybe not the best either but I found it smooth
            setLayout(new GridBagLayout());
            cons= new GridBagConstraints();
            //connection button
            Action connectionButton = new AbstractAction("CONNECTION"){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    connectionEvent();
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
                        connectionEvent();
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
            frame.setResizable(true);
            frame.setContentPane(this);
        }
    }
    /**Interface on which the user chooses who to chat with, refreshable*/
    public class ChooseDiscussionInterface extends Container implements Observers.ObserverConnection,Observers.ObserverDisconnection {
        JFrame frame;
        ArrayList<User> activeusers;

        /**
         * new JButton containing a user to be able to find and modify a Button
         */
        public class UserButton extends JButton {
            public User user;
            public UserButton(Action a,User user){
                super(a);
                this.user=user;
            }
            public User getUser(){
                return user;
            }
        }

        /**
         * Observer function to change/add a button when a user changes its pseudo/ connects
         * @param user who's concerned
         */
        @Override
        public void userConnected(User user) {
            LOGGER.debug("New user or user pseudo changed, updating buttons.");
            for (Component c : this.getComponents()){
                if (c instanceof UserButton && ((UserButton) c).getUser().equals(user.getHostname())){
                    this.remove(c);
                }
            }
            Action userButton = new AbstractAction(user.getPseudo() ){
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    setVisible(false);
                    new ChatInterface(frame,user);
                }
            };
            add(new UserButton(userButton,user));
            frame.revalidate();
            frame.repaint();
        }
        /**
         * Observer removing button when user disconnects
         * @param user who's concerned
         */
        @Override
        public void userDisconnected(User user){
            LOGGER.debug("User disconnected "+user.getHostname());
            for (Component c : this.getComponents()){
                if (c instanceof UserButton && ((UserButton) c).getUser().equals(user.getHostname())){
                    this.remove(c);
                }
            }
            frame.repaint();
            frame.revalidate();
        }

        /**Constructor
         * @param frame in activity
         * */
        public ChooseDiscussionInterface(JFrame frame) {
            this.frame=frame;
            ThreadManager.getInstance().getUdpServer().attachConnection(this);
            ThreadManager.getInstance().getUdpServer().attachDisconnection(this);
            activeusers = ActiveUserManager.getInstance().getListActiveUser();
            //gridlayout, maybe not the best
            setLayout( new GridLayout(activeusers.size(),1));
            //creates buttons for each users
            for (User user: activeusers){
                Action userButton = new AbstractAction(user.getPseudo()) {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        setVisible(false);
                        new ChatInterface(frame,user);
                    }
                };
                add(new UserButton(userButton,user));
            }
            //create menu
            JMenuBar bar = new JMenuBar();
            JMenu file = new JMenu("Menu");
            file.add(changePseudoButton);
            file.add(disconnectionButton);
            bar.add(file);
            frame.setJMenuBar(bar);
            frame.setResizable(true);
            frame.setContentPane(this);
        }
    }
    /**Chat Interface: send messages, append received ones, interact with the user, Highly smooth*/
    public class ChatInterface extends Container implements Observers.ObserverReception, Observers.ObserverDisconnection {
        JTextArea chatArea;
        JTextField inputArea;
        JFrame frame;
        User dest;
        ConversationsManager db;

        Action clearConversationButton = new AbstractAction("CLEAR CONVERSATION"){
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                ConversationsManager.getInstance().clearConversation(dest.getHostname());
            }
        };

        private final ArrayList<Observers.ObserverReception> observers = new ArrayList<>();
        public void attach(Observers.ObserverReception observer){
            this.observers.add(observer);
        }

        public void notifyObserver(Message mess){
            for (Observers.ObserverReception observer: observers){
                observer.messageReceived(mess);
            }
        }

        /**Constructor, same as ChooseDiscussionInterface, the core of the interface is specified in initComponents
         * @param dest User to discuss with
         * @param frame - frame in activity
         * */
        public ChatInterface(JFrame frame, User dest){
            this.frame=frame;
            this.dest = dest;
            LOGGER.info("starting conversation with user:"+dest);
            this.db = ConversationsManager.getInstance();
            initComponents(); //specify interface components
            displayOldMessages(); //crystal clear
            this.attach(ConversationsManager.getInstance());
            this.attach(ThreadManager.getInstance());
            ThreadManager.getInstance().getUdpServer().attachDisconnection(this);
            //here we make sure the TCPClientHandler thread associated to our conversation exists, then we observe it
            TCPClientHandler tcpClientHandler  = ThreadManager.getInstance().getActiveconversation(dest);
            if (tcpClientHandler==null){
                Socket socket= null;
                try {
                    socket = new Socket(dest.getHostname(), Self.portTCP);
                } catch (IOException e) {
                    LOGGER.error("Unable to create TCP socket. Hostname: "+ dest.getHostname()+" Port TCP: "+Self.portTCP);
                    e.printStackTrace();
                    new ChooseDiscussionInterface(frame);
                }
                tcpClientHandler = new TCPClientHandler(socket,dest);
                tcpClientHandler.start();
                ThreadManager.getInstance().addActiveconversation(dest,tcpClientHandler);
            }
            tcpClientHandler.attach(this);
            frame.setContentPane(this);
            frame.setSize(new java.awt.Dimension(510, 470));
        }
        /** Initialize the components of the interface */
        private void initComponents(){
            //declaration of variables
            inputArea = new JTextField();
            JScrollPane jScrollPane1 = new JScrollPane();
            chatArea = new JTextArea();
            JLabel discussionName = new JLabel();
            setLayout(null); //layout positioning via coordinates
            //inputArea specify
            inputArea.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode()== KeyEvent.VK_ENTER){
                        sendMessage(inputArea.getText());
                        inputArea.setText("");
                    }
                }
            }); //send message when enter pressed in input area

            //send button specify
            ImageIcon sendIcon = (new ImageIcon(Objects.requireNonNull(ChatInterface.class.getClassLoader().getResource("send.png"))));
            Image image = sendIcon.getImage();
            image = image.getScaledInstance(40, 40,  Image.SCALE_SMOOTH);
            sendIcon = new ImageIcon(image);
            JButton send = new JButton();
            send.setContentAreaFilled(true);
            send.setIcon(sendIcon);
            send.setOpaque(false);
            send.setContentAreaFilled(false);
            send.setBorderPainted(false);
            send.addActionListener(evt -> {
                sendMessage(inputArea.getText());
                inputArea.setText("");
            });

            //chatArea specify
            chatArea.setColumns(20);
            chatArea.setRows(5);
            chatArea.setEditable(false);
            chatArea.setWrapStyleWord(true);
            chatArea.setLineWrap(true);
            jScrollPane1.setViewportView(chatArea);


            //discussionName specify
            discussionName.setFont(new Font("Myriad Pro", Font.PLAIN, 30)); // NOI18N
            discussionName.setForeground(foregroundColor);
            discussionName.setText(dest.getPseudo());

            //add
            add(inputArea);
            add(send);
            add(jScrollPane1);
            add(discussionName);

            //positionning
            send.setBounds(450, 370, 40, 40);
            jScrollPane1.setBounds(10, 80, 490, 280);
            discussionName.setHorizontalAlignment(SwingConstants.CENTER);
            discussionName.setBounds(0, 20, 480, 40);
            inputArea.setBounds(10, 370, 410, 40);
            setSize(new Dimension(510, 480));

            //menu bar
            JMenuBar bar = new JMenuBar();
            JMenu file = new JMenu("Menu");
            file.add(changeDiscussionButton);
            file.add(changePseudoButton);
            file.add(disconnectionButton);
            file.add(clearConversationButton);
            bar.add(file);
            frame.setJMenuBar(bar);
            frame.setResizable(false);
        }

        /**Update method which appends new received messsages when TCPClient says he received some
         * @param mess Message the tcpclienthanlder received
         * */
        @Override
        public void messageReceived(Message mess){
            chatArea.append("\n("+ mess.getSender().getPseudo()+") - "+mess.getMessage());
        }

        /** Shows a disconnected interface if the user we were chatting with disconnected
         * */
        @Override
        public void userDisconnected(User user){
            if (dest.equals(user.getHostname())){
                //if we are chatting with the user right now it shows a user-disconnected interface
                JOptionPane.showMessageDialog(frame, "The user you were chatting with just disconnected. " +
                                "As messages won't get through, you will get back to the choose discussion interface in 3 secs.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setVisible(false);
                //close discussion
                frame.setResizable(true);
                new ChooseDiscussionInterface(frame);
            }
        }

        /**Send messages to the destination user
         * @param message to send
         * */
        private void sendMessage(String message) {
            Self selfInstance = Self.getInstance();
            Message mess= new Message(new User(selfInstance.getHostname(),selfInstance.getPseudo()), dest,message);
            chatArea.append("\nME("+ Self.getInstance().getPseudo()+") - "+message);
            notifyObserver(mess);
        }
        /**Display old messages at the opening of the chat interface*/
        private void displayOldMessages() {
            try {
                ArrayList<Message> conv = db.getAllMessages(dest.getHostname());
                for (Message message: conv){
                    if ((message.getSender().getHostname()).equals(Self.getInstance().getHostname()))
                    {
                        chatArea.append("\nME("+ Self.getInstance().getPseudo()+") - "+message.getMessage());
                    }
                    else {
                        chatArea.append("\n("+message.getSender().getPseudo()+") - "+message.getMessage());
                    }
                }
            }catch(MessageAccessProblem e){
                LOGGER.error("Error loading existing messages from the Database. Or no messages for this conversation.");
            }
        }
    }
}
