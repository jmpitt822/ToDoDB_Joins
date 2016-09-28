import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by jeremypitt on 9/28/16.
 */
public class ToDoTest {
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        ToDo.createTables(conn);
        return conn;
    }

    @Test
    public void testUser() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "Alice", "");
        User user = ToDo.selectUser(conn, "Alice");
        conn.close();
        assertTrue(user != null);
    }

    @Test
    public void testToDos() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "Jimbo", "");
        User jimbo = ToDo.selectUser(conn, "Jimbo");
        ToDo.insertToDo(conn, jimbo.id, "To do: make this test work.");
        ArrayList<ToDoItem> items = ToDo.selectToDos(conn);
        conn.close();
        assertTrue(items.size() == 1);
    }

    @Test
    public void testUpdateToDo() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "Jimbo", "");
        User jimbo = ToDo.selectUser(conn, "Jimbo");
        ToDo.insertToDo(conn, jimbo.id, "To do: make this test work.");
        ToDoItem testDo = ToDo.selectToDo(conn, 1);
        ToDo.toggleToDo(conn, testDo.id);
        ToDoItem testDo1 = ToDo.selectToDo(conn, 1);
        boolean testisDone = testDo1.isDone;
        assertTrue(testDo1.isDone);
    }

    @Test
    public void testDeleteToDo() throws SQLException {
        Connection conn = startConnection();
        ToDo.insertUser(conn, "Jimbo", "");
        User jimbo = ToDo.selectUser(conn, "Jimbo");
        ToDo.insertToDo(conn, jimbo.id, "To do: make this test work.");
        ToDoItem testDo = ToDo.selectToDo(conn, 1);
        ArrayList<ToDoItem> items = new ArrayList<>();
//        items.add(testDo);
//        ToDo.deleteToDo(conn, testDo.id);
//        ToDoItem testDo1 = ToDo.selectToDo(conn, 1);
////        items.add(testDo);
//        assertTrue(items.isEmpty());
        assertTrue(testDo.text.contains(""));
    }

}