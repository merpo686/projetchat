package Conversations;

/**exception that occurs when the server is unable to access the messages with designated hostname*/
public class MessageAccessProblem extends Exception {
    final String hostname;
    public MessageAccessProblem(String pseudo){
        this.hostname = pseudo;
    }

    @Override
    public String toString(){
        return "MessageAccessProblem:\n" + "{Unable to access messages with designed hostname : " + hostname + " [maybe caused because of a null pointer exception in listMessages]}";
    }
}
