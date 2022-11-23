package Managers;
import Models.User;
import java.util.ArrayList;


public class ActiveUserManager {
    private User U;
    static ArrayList<User> listActiveUsers;
    public ActiveUserManager(User U){
        this.U = U;
        this.listActiveUsers = new ArrayList<User>();
    }
    public static void addListActiveUser(User U){
        listActiveUsers.add(U);
    }

    public static void removeListActiveUser(User U){
        listActiveUsers.remove(U);
    }

    ArrayList<User> getListActiveUser(){
        return this.listActiveUsers;
    }

    public static boolean IsinActiveListUser(String Pseudo){
        for (User user: listActiveUsers){
            if (user.get_Pseudo()==Pseudo){
                return true;
            }
        }
        return false;
    }
}
