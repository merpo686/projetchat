package Models;
//2
public class Validation extends Notifications{
    String Pseudo;
    Boolean Valid;
    public Validation(int type, String Pseudo, boolean Valid){
        super(type);
        this.Pseudo=Pseudo;
        this.Valid=Valid;
    }
    public boolean get_Valid(){
        return this.Valid;
    }
    public String getPseudo(){
        return this.Pseudo;
    }
}
