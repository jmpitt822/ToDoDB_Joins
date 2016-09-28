/**
 * Created by jeremypitt on 9/6/16.
 */
public class ToDoItem {
    public int id;
    public int userId;
    public String text;
    public boolean isDone;

    public ToDoItem(String text, boolean isDone) {
        this.text = text;
        this.isDone = isDone;
    }

    public ToDoItem(int id, String text, boolean isDone) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
    }

    public ToDoItem(int id, int userId, String text, boolean isDone) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.isDone = isDone;
    }
}
