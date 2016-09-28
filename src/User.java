/**
 * Created by jeremypitt on 9/28/16.
 */
public class User {
    int id;
    String name;
    String password;

    public User(String name) {
        this.name = name;
    }

    public User(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}
