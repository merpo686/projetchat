package database;
import Models.Message;
import Models.User;
import org.sqlite.SQLiteConfig;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.nio.file.*;

public class DatabaseManager {
    private Connection co;
    private String databaseName = "messages.sqlite";
    static DatabaseManager instance;

    private DatabaseManager() throws ConnectionError {
        this.connectDB(databaseName);
    }

    public static DatabaseManager getInstance() throws ConnectionError {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connectDB(String databaseName) throws ConnectionError {
        String url = "jdbc:sqlite:" + databaseName;
        //the driver automatically creates a new database when the database does not already exist
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            co = DriverManager.getConnection(url, config.toProperties());
            DatabaseMetaData meta = co.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while(rs.next()){
                String catalog = rs.getString(1); //retrieves the second column of the result set corresponding to the catalogs which stores the names of all the databases
                if(!catalog.equals(databaseName)){ //check if the database exists already
                    System.out.println("Creating a new database...");
                }
            }
            System.out.println("A connection has been established");
            System.out.println("The driver name is " + meta.getDriverName());
        } catch (SQLException throwables1) {
            throwables1.printStackTrace();
            throw new ConnectionError(url);
        } /*finally {
            try {
                if(co != null){
                    co.close();
                }
            }
            catch(SQLException throwables2) {
                throwables2.printStackTrace();
                throw new ConnectionError(url);
            }*/
        //}
    }

    public void createTableConversations() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Conversations (\n" +
                "Hostname STRING PRIMARY KEY NOT NULL);";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.executeUpdate();
        ps.clearParameters();
    }

    public void createTableMessages(String hostname) throws SQLException {
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
                "Hostname STRING NOT NULL, \n" +
                "FOREIGN KEY(Hostname) REFERENCES Conversations(Hostname));";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.executeUpdate();
        /*ps.setString(1, hostname);*/
        /*ps.executeUpdate();
        ps.clearParameters();
        SELECT IdConv FROM Conversations WHERE (Conversations.Hostname = ?)*/
    }

<<<<<<< HEAD
    public void addMessage(Message message, String hostnameConv) throws SQLException {
        String stringMessage = message.getMessage();
        Date date = (Date) message.getDate();
        String senderID = message.getSender().getHostname();
        String senderPseudo = message.getSender().getPseudo();
        String receiverID = message.getReceiver().getHostname();
        String receiverPseudo = message.getSender().getPseudo();
        Statement statement = co.createStatement();
        //verifier si la conv existe sinon la creer
        String sql = "INSERT INTO hostnameConv \n" +
                "VALUES(Id, date, senderID, senderPseudo, receiverID, receiverPseudo, stringMessage);";
        statement.executeUpdate(sql);
=======
    public void addConversation(String hostname) throws SQLException {
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        PreparedStatement ps = co.prepareStatement("INSERT INTO Conversations (Hostname) VALUES(?)");
        ps.setString(1, hostname);
        ps.executeUpdate();
        ps.clearParameters();
    }

    public void addMessage(Message message, String hostname) throws SQLException { //A VOIR SI ON PEUT PAS REDUIR LE NB DE PARAMETRES A 1 ET ENLEVER LE HOSTNAME CAR IL EST NORMALEMENT EGAL AU MESSAGE RECEIVER
        String stringMessage = message.get_message();
        LocalDateTime date = message.get_date();
        String senderID = message.get_sender().get_Hostname();
        String senderPseudo = message.get_sender().get_Pseudo();
        String receiverID = message.get_receiver().get_Hostname();
        String receiverPseudo = message.get_receiver().get_Pseudo();
        //verifier si la conv existe sinon la creer et s'il existe une table de conversations tout court
        if(!checkExistTableConversations()){
            createTableConversations();
        }
        if(!checkExistConversation(hostname)){
            addConversation(hostname);
        }
        if(!checkExistTableMessages()){
            createTableMessages(hostname);
        }

        PreparedStatement ps = co.prepareStatement("INSERT INTO Messages (IdMessage, Date, SenderID, SenderPseudo, ReceiverID, ReceiverPseudo, StringMessage, Hostname) VALUES(?, ?, ?, ?, ?, ?, ?, ?);");
        ps.setString(2, String.valueOf(date));
        ps.setString(3, senderID);
        ps.setString(4, senderPseudo);
        ps.setString(5, receiverID);
        ps.setString(6, receiverPseudo);
        ps.setString(7, stringMessage);
        ps.setString(8, hostname);
        ps.executeUpdate();
        ps.clearParameters();
>>>>>>> 883ed292846ee596c910df3a9443aebf1242c285
    }

    //PAS BON ENCORE FAUT TROUVER UN MOYEN POUR FAIRE EXECUTE UPDATE ET RECUP LES MESSAGES
    public Message getLastMessage(String hostname) throws SQLException, UnknownHostException {
        String sql = "SELECT LAST(StringMessage) FROM ?;";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.setString(1, hostname);
        ResultSet rs = ps.executeQuery(sql);
        ps.clearParameters();
<<<<<<< HEAD
        Message mess = new Message(new User(rs.getString(3),rs.getString(4)),
                new User(rs.getString(5), rs.getString(6)), rs.getString(7));
        String stringDate = rs.getString(2);
        mess.set_date(LocalDateTime.parse(stringDate));
        return mess;
    }

    public ArrayList<Message> getAllMessages(String hostname) throws  MessageAccessProblem {
        try{
            ArrayList<Message> listMessages = null;
            String sql = "SELECT * FROM Messages WHERE (hostname = ?);";
            PreparedStatement ps = co.prepareStatement(sql);
            ps.setString(1, hostname);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                User sender = new User(rs.getString(3),rs.getString(4));
                User receiver = new User(rs.getString(5), rs.getString(6));
                Message mess = new Message(sender, receiver, rs.getString(7));
                String stringDate = rs.getString(2);
                mess.set_date(LocalDateTime.parse(stringDate));
                listMessages.add(mess);
            }
            return listMessages;
        } catch(Exception e){
            e.printStackTrace();
            throw new MessageAccessProblem(hostname);
=======
        Message mess = new Message(new User(rs.getString(2),rs.getString(3)),
                new User(rs.getString(4), rs.getString(5)), rs.getString(6));
        mess.setDate(rs.getDate(2));
        return mess;
    }

    public ArrayList<Message> getAllMessages(String hostnameConv) throws SQLException, UnknownHostException {
        ArrayList<Message> listMessages = null;
        Statement statement = co.createStatement();
        String sql = "SELECT * FROM hostnameConv;";
        ResultSet rs = statement.executeQuery(sql);
        /*String length = "COUNT * FROM hostnameConv";
        ResultSet rsLength = statement.executeQuery(sql);*/
        while(rs.next()) {
            Message mess = new Message(new User(rs.getString(2),rs.getString(3)),
                    new User(rs.getString(4), rs.getString(5)), rs.getString(6));
            mess.setDate(rs.getDate(2));
            listMessages.add(mess);
        }
        if(listMessages == null){
            System.out.println("You have no existing messages with the designated user"); //il faudrait que ca remonte au user qu'il y ai aucun message
>>>>>>> ab06e4752b3db66e082810fb4c78cca06631d21c
        }
    }

    //checks if the entire conversations table exists
    public Boolean checkExistTableConversations() throws SQLException {
        Statement statement = co.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Conversations'";
        ResultSet rs = statement.executeQuery(sql);
        //checks if the table is null
        if (rs.getString(1).equals("0")){
            System.out.println("The conversations table does not exist");
            return false;
        }
        else {
            System.out.println("The conversation table exists");
            return true;
        }
    }

    //checks if a single conversation exists inside the table conversations
    public Boolean checkExistConversation(String hostname) throws SQLException {
        String sql = "SELECT EXISTS(SELECT Hostname FROM Conversations WHERE Conversations.Hostname = ?);";
        PreparedStatement ps = co.prepareStatement(sql);
        ps.setString(1, hostname);
        ResultSet rs = ps.executeQuery();

        //checks if the table is null
        if (rs.getString(1).equals("0")){
            System.out.println("The conversation "+hostname+" does not exist");
            return false;
        }
        else {
            System.out.println("The conversation "+hostname+" exists");
            return true;
        }
    }

    //checks if the entire messages table exists
    public Boolean checkExistTableMessages() throws SQLException {
        Statement statement = co.createStatement();
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='Messages'";
        ResultSet rs = statement.executeQuery(sql);
        //checks if the table is null
        if (rs.getString(1).equals("0")){
            System.out.println("The messages table does not exist");
            return false;
        }
        else {
            System.out.println("The messages table exists");
            return true;
        }
    }

    public void clearDB(String databaseName) {
        Path path = Path.of(databaseName);
        System.out.println(path.toAbsolutePath());
        try {
            Files.delete(path);
        } catch(NoSuchFileException e) {
            System.out.println("Delete: No such file directory " + path);
        } catch(DirectoryNotEmptyException e){
            System.out.println("Directory is not empty" + path);
        } catch(IOException e) {
            System.out.println("You don't permission" + path);
        }
        System.out.println("File deleted successfully: " + path);
    }
    
    public String getDBName(){return this.databaseName;}
}
