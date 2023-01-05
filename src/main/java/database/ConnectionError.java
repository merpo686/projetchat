package database;

public class ConnectionError extends Exception {

    final String url;
    public ConnectionError(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ConnectionError{" +
                "url='" + url + '\'' +
                '}';
    }
}
