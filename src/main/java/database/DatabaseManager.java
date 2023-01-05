package database;
import Models.Conversation;
import org.sqlite.*;
import java.sql.*;

public class DatabaseManager {
    private String DataBaseName;
    private Connection co;
    public DatabaseManager(String DataBaseName) {
        this.DataBaseName = DataBaseName;
    }

    public void connectDB() throws ConnectionError {
        String url = "jdbc:sqlite:C:/sqlite/db/" + DataBaseName;
        //the driver automatically creates a new database when the database does not already exist
        try {
            co = DriverManager.getConnection(url);
            DatabaseMetaData meta = co.getMetaData();
            ResultSet rs = meta.getCatalogs();
            while(rs.next()){
                String catalog = rs.getString(1); //retrieves the second column of the result set corresponding to the catalogs which stores the names of all the databases
                if(!catalog.equals(DataBaseName)){ //check if the database exists already
                    System.out.println("Creating a new database...");
                }
            }
            System.out.println("The driver name is " + meta.getDriverName());
            System.out.println("A connection has been established for " + meta.getUserName());
        } catch (SQLException throwables1) {
            throwables1.printStackTrace();
            throw new ConnectionError(url);
        } finally {
            try {
                if(co != null){
                    co.close();
                }
            }
            catch(SQLException throwables2) {
                throwables2.printStackTrace();
                throw new ConnectionError(url);
            }
        }
    }

    public void createTableConversations() throws SQLException {
        Statement statement = co.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Conversations (\n" +
                "Id AS Integer NOT NULL AUTO INCREMENT\n" +
                "Conversation AS Conversation\n" +
                "PRIMARY KEY(Id));"; //a tester la requete hein
        statement.execute(sql);
    }

    public void addConversation(Conversation conv) throws SQLException {
        Statement statement = co.createStatement();
        String sql = "INSERT INTO Conversations \n" +
                "VALUES(Id, Conv);";
        statement.executeUpdate(sql);
    }
}
