package Managers;
import Models.User;

import java.util.ArrayList;

/**
 * Class containing the list of active users
 */
public class ActiveUserManager {
    static ActiveUserManager instance;
    ArrayList<User> listActiveUsers;
    /**Constructor initiates the Active user list*/
    private ActiveUserManager() {
        listActiveUsers = new ArrayList<>();
    }
    /**Creates a new instance of Active user manager */
    public static ActiveUserManager getInstance() {
        if (instance == null) {
            instance = new ActiveUserManager();
        }
        return instance;
    }
    /**to add a user to the list
     * @param U - user to add
     * */
    public void addListActiveUser(User U){
        listActiveUsers.add(U);
    }
    /**removes and add a user who changed pseudo
     * @param U - user to change
     */
    public void changeListActiveUser(User U){
        if (IsinActiveListUser(U.getHostname())){
            removeListActiveUser(U.getHostname());
        }
        addListActiveUser(U);
    }
/** removes a user
 * @param hostname of the user to remove
 * */
    public void removeListActiveUser(String hostname){
        listActiveUsers.removeIf(u -> u.equals(hostname));
    }
/**returns the list*/
    public ArrayList<User> getListActiveUser() {
        return listActiveUsers;
    }
/** check if a user is active
 * @param hostname of the user to check
 * */
    public boolean IsinActiveListUser(String hostname){
        try {
            for (User user: listActiveUsers){
                if (user.equals(hostname)){
                    return true;
                }
            }
        }
        catch (NullPointerException e){
            return false;
        }
        return false;
    }
/** returns a user if it is active else returns null
 * @param hostname of the user to get
 * */
    public User get_User(String hostname){
        try {
            for (User user: listActiveUsers){
                if (user.equals(hostname)){
                    return user;
                }
            }
        }
        catch (NullPointerException e){
            return null;
        }
        return null;
    }
    /** toString */
    public String toString(){return listActiveUsers.toString();}
}
