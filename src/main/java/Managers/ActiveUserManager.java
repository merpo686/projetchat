package Managers;
import Models.User;
import java.util.ArrayList;


public class ActiveUserManager {
    public ActiveUserManager(User U){
        this.User = U;
        ArrayList<User> listActiveUsers = new ArrayList<User>();
    }
    void addListActiveUser(User U){
        listActiveUsers.add(U);
    }

    void removeListActiveUser(User U){
        listActiveUsers.remove(U);
    }
}
