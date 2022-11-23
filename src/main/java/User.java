import Managers.UserManager;

public class User {
    public static void main(){
        try {
            UserManager Um= new UserManager();
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
