package Graphics;


import Models.User;


import java.awt.*;


/** Class which contains the name of the current used interface "state",
 *  the user with which we are discussing (if relevant), and some static parameters like the colors*/
public class InterfaceManager {
    public static Color backgroundColor = new Color(15,5, 107); //colors by default of the interface
    public static Color foregroundColor = new Color(252,210,28);
    String state;
    User userDiscussion;
    static InterfaceManager instance;

    /**
     * Constructor
     */
    private InterfaceManager() {
         userDiscussion =null;
         state=null;
    }

    /**
     * Return the interface in activity
     * @return state - string name of the interface
     */
    public String getState(){return state;}

    /**
     * Return User with whom we're discussing if relevant
     * @return userDiscussion
     */
    public User getUser(){return userDiscussion;}

    /**
     * Change interface in activity
     * @param state - string name of the interface
     */
    public void setState(String state){this.state=state;}

    /**
     * Change user with whom we discuss
     * @param user
     */
    public void setUser(User user){this.userDiscussion =user;}

    public static InterfaceManager getInstance() {
        if (instance == null) {
            instance = new InterfaceManager();
        }
        return instance;
    }
}
