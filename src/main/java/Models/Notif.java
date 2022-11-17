package Models;

public class Notif extends Notifications {
    String Pseudo;
    public Notif(int type, String Pseudo){
        super(type);
        this.Pseudo=Pseudo;
    }
}
