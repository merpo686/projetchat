package Models;
//3
public class Notif extends Notifications {
    String Pseudo;
    public Notif(int type, String Pseudo){
        super(type);
        this.Pseudo=Pseudo;
    }
    public String get_Pseudo(){return this.Pseudo;}
}
