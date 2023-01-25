package Conversations;
import ActivityManagers.Self;
import Models.Message;
import Models.Observers;
import Models.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.sqlite.SQLiteConfig;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.nio.file.*;
public class ConversationsManager implements Observers.ObserverMessage {
    private Connection co;
    private final String databaseName = "messages.sqlite";
    static ConversationsManager instance;
    private static final Logger LOGGER = LogManager.getLogger(ConversationsManager.class);
    /**
     * Constructor
     */
    private ConversationsManager(){
        try {
            this.connectDB(databaseName);
        } catch (ConnectionError connectionError) {
            LOGGER.error("Can't make connection to the database");
            connectionError.printStackTrace();
        }
    }
    /**the get instance() for the database manager, as we want to be able to access it at any time from any function*/
    public static ConversationsManager getInstance() {
        if (instance == null) {
            instance = new ConversationsManager();
        }
        return instance;
    }
    /**
     * Connects to database
     * @param databaseName the database to connect to
     * @throws ConnectionError if connection fails
     */
    public synchronized void connectDB(String databaseName) throws ConnectionError {
        String url = "jdbc:sqlite:" + databaseName;
        //the driver automatically creates a new database when the database does not already exist
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            co = DriverManager.getConnection(url, config.toProperties());
            DatabaseMetaData meta = co.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while(rs.next()){
                String catalog = rs.getString(1); //retrieves the first column of the result set corresponding to the catalogs which stores the names of all the databases
                if(!catalog.equals(databaseName)){ //check if the database exists already
                    System.out.println("Creating a new database...");
                }
            }
            System.out.println("A connection has been established");
            System.out.println("The driver name is " + meta.getDriverName());
        } catch (SQLException throwables1) {
            throwables1.printStackTrace();
            throw new ConnectionError(url);
        }
    }
    /**
     * creates a table for the conversations to be stored
     * @throws SQLException
     */
    public synchronized void createTableConversations() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Conversations (\n" +
                "IdConv INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "Hostname STRING NOT NULL);";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.executeUpdate();
        ps.clearParameters();
    }
    /**
     * creates a table for the messages to be stored in a specific conversation identified by hostname,
     * which corresponds to the hostname of the receiver
     * @param hostname
     * @throws SQLException
     */
    public synchronized void createTableMessages(String hostname) throws SQLException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        String sql = "CREATE TABLE IF NOT EXISTS Messages (\n" +
                "IdMessage INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "Date STRING NOT NULL, \n" +
                "SenderID STRING NOT NULL, \n" +
                "SenderPseudo STRING NOT NULL, \n" +
                "ReceiverID STRING NOT NULL, \n" +
                "ReceiverPseudo STRING NOT NULL, \n" +
                "StringMessage STRING NOT NULL, \n" +
                "IdConv INTEGER NOT NULL, \n" +
                "FOREIGN KEY(IdConv) REFERENCES Conversations(IdConv));";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.executeUpdate();
    }
    /**
     * adds a conversation identified by the hostname of the receiver in the conversations table
     * @param hostname
     * @throws SQLException
     */
    public synchronized void addConversation(String hostname) throws SQLException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        PreparedStatement ps = co.prepareStatement("INSERT INTO Conversations (IdConv, Hostname) VALUES(?, ?)");
        ps.setString(2, hostname);
        ps.executeUpdate();
        ps.clearParameters();
    }
    /**
     * adds a message in the messages table
     * @param message
     * @throws SQLException
     * @throws ConnectionError
     */
    public synchronized void addMessage(Message message) throws SQLException, ConnectionError {
        int idConv = 0;
        String stringMessage = message.getMessage();
        LocalDateTime date = message.getDate();
        String senderID = message.getSender().getHostname();
        String senderPseudo = message.getSender().getPseudo();
        String receiverID = message.getReceiver().getHostname();
        String receiverPseudo = message.getReceiver().getPseudo();
        //we store messages with the hostname of the other user as a key
        String hostname = receiverID;
        if (hostname.equals(Self.getInstance().getHostname())){
            hostname=senderID;
        }
        //check if the tables and corresponding conversation exist
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        if(!checkExistTableMessages()){
            createTableMessages(hostname);
        }
        //we acquire the Id of the table Conversations (IdConv), which is the foreign key for the table messages
        PreparedStatement ps1 = co.prepareStatement("SELECT IdConv FROM Conversations WHERE Conversations.Hostname == ?");
        ps1.setString(1, hostname);
        ResultSet rs = ps1.executeQuery();
        while(rs.next()){
            idConv = rs.getInt("IdConv");
        }
        PreparedStatement ps2 = co.prepareStatement("INSERT INTO Messages (IdMessage, Date, SenderID, SenderPseudo, ReceiverID, ReceiverPseudo, StringMessage, IdConv) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
        ps2.setString(2, String.valueOf(date));
        ps2.setString(3, senderID);
        ps2.setString(4, senderPseudo);
        ps2.setString(5, receiverID);
        ps2.setString(6, receiverPseudo);
        ps2.setString(7, stringMessage);
        assert idConv != 0;
        ps2.setInt(8, idConv);
        ps2.executeUpdate();
        ps2.clearParameters();
        System.out.println("Message added to the database to designated user " + hostname);
    }
    /**
     * gets the last message sent by the sender in a specific conversation identified by hostname, which corresponds to the hostname of the receiver
     * @param hostname
     * */
    public Message getLastMessage(String hostname) throws SQLException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        if(!checkExistTableMessages()){
            createTableMessages(hostname);
        }
        String sql = "SELECT * FROM Messages WHERE (ReceiverID = ?) ORDER BY ReceiverID DESC LIMIT 1;";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.setString(1, hostname);
        ResultSet rs = ps.executeQuery();
        ps.clearParameters();
        if(rs.next()){
            Message mess = new Message(new User(rs.getString(3),rs.getString(4)),
                    new User(rs.getString(5), rs.getString(6)), rs.getString(7));
            String stringDate = rs.getString(2);
            mess.setDate(LocalDateTime.parse(stringDate));
            return mess;
        }
        return null;
    }
    /**
     * gets all messages sent by the sender in a specific conversation identified by hostname, which corresponds to the hostname of the receiver
     * @param hostname
     * @return an array list of messages corresponding to the conversation
     * @throws MessageAccessProblem
     */
    public ArrayList<Message> getAllMessages(String hostname) throws  MessageAccessProblem {
        try{
            if(!checkExistTableConversations()){
                createTableConversations();
            }
            if(!checkExistConversation(hostname)){
                addConversation(hostname);
            }
            if(!checkExistTableMessages()){
                createTableMessages(hostname);
            }
            ArrayList<Message> listMessages = new ArrayList<>();
            String sql = "SELECT * FROM Messages WHERE (ReceiverID = ?) OR (SenderID = ?);";
            PreparedStatement ps = co.prepareStatement(sql);
            ps.setString(1, hostname);
            ps.setString(2,hostname);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User sender = new User(rs.getString(3),rs.getString(4));
                User receiver = new User(rs.getString(5), rs.getString(6));
                Message mess = new Message(sender, receiver, rs.getString(7));
                String stringDate = rs.getString(2);
                mess.setDate(LocalDateTime.parse(stringDate));
                listMessages.add(mess);
            }
            return listMessages;
        } catch(Exception e){
            e.printStackTrace();
            throw new MessageAccessProblem(hostname);
        }
    }
    /**
     * checks if the entire conversations table exists
     * @return
     * @throws SQLException
     */
    public boolean checkExistTableConversations() throws SQLException {
        int count=0;
        Statement statement = co.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Conversations'";
        ResultSet rs = statement.executeQuery(sql);
        while(rs.next()){
            count++;
        }
        return count == 1;
    }
    /**
     * checks if a specific conversation exists inside the conversations table
     * @param hostname
     * @return
     */
    public boolean checkExistConversation(String hostname) {
        try {
            if(!checkExistTableConversations()){
                createTableConversations();
            }
            String sql = "SELECT EXISTS(SELECT 1 FROM Conversations WHERE Conversations.Hostname = ?);";
            PreparedStatement ps = co.prepareStatement(sql);
            ps.setString(1, hostname);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String stringExist = rs.getString(1);
                if (stringExist.equals("1")){ //SELECT EXISTS returns 1 if the query exists, 0 if not
                    return true;
                }
            }
            return false;
        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    /**
     * checks if the entire messages table exists
     * @return
     * @throws SQLException
     */
    public boolean checkExistTableMessages() throws SQLException {
        int count=0;
        Statement statement = co.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Messages'";
        ResultSet rs = statement.executeQuery(sql);

        while(rs.next()){
            count++;
        }
        return count == 1;
    }
    /**
     * clears the database specified
     * @param databaseName of the database to clear
     */
    public synchronized void clearDB(String databaseName) {
        Path path = Path.of(databaseName);
        System.out.println(path.toAbsolutePath());
        try {
            Files.delete(path);
        } catch(NoSuchFileException e) {
            System.out.println("Delete: No such file directory " + path);
        } catch(DirectoryNotEmptyException e){
            System.out.println("Directory is not empty" + path);
        } catch(IOException e) {
            System.out.println("You don't have permission" + path);
        }
        System.out.println("File deleted successfully: " + path);
    }
    public void clearConversation(String hostname){
        try{
            String sql = "DELETE FROM Messages WHERE (idConv = ?);";
            PreparedStatement ps = co.prepareStatement(sql);
            ps.setString(1, hostname);
            ps.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    /**Update method which appends new received messages when TCPClient says he received some
     * or when ChatInterface sends one
     * @param mess Message the TCPClientHandler received
     * */
    @Override
    public void messageHandler(Message mess){
        try {
            this.addMessage(mess);
        } catch (SQLException | ConnectionError throwables) {
            throwables.printStackTrace();
        }
    }
}
