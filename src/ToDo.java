import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ToDo {
    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS todos (id IDENTITY, user_id INT, text VARCHAR, is_done BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY , name VARCHAR , password VARCHAR )");
    }

    public static void insertToDo(Connection conn, int userId, String text) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO todos VALUES (NULL, ?, ?, FALSE)");
        stmt.setInt(1, userId);
        stmt.setString(2, text);
        stmt.execute();
    }

    public static ToDoItem selectToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos INNER JOIN users ON todos.user_id = users.id WHERE todos.id = ?");
        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            String text = results.getString("text");
            boolean is_done = results.getBoolean("is_done");
            return new ToDoItem(id, text, is_done);
        }
        return null;
    }
    //"SELECT * FROM messages INNER JOIN users ON messages.user_id = users.id WHERE messages.id = ?"
    public static ArrayList<ToDoItem> selectToDos(Connection conn) throws SQLException{
        ArrayList<ToDoItem> items = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM todos INNER JOIN users ON todos.user_id = users.id ");
//        stmt.setInt(1, id);
        ResultSet results = stmt.executeQuery();
        while (results.next()){
            int id = results.getInt("id");
            String text = results.getString("text");
            boolean is_done = results.getBoolean("is_done");
            ToDoItem item =new ToDoItem(id, text, is_done);
            items.add(item);
        }
        return items;
    }

    public static void toggleToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE todos SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void deleteToDo(Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM todos WHERE todos.id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    public static void insertUser(Connection conn, String name, String password) throws SQLException{
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (null, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if(results.next()){
            int id = results.getInt("id");
            String password = results.getString("password");
            return new User(id, name, password);
        }
        return null;
    }

    public static void main(String[] args) throws SQLException{
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Scanner scanner = new Scanner(System.in);
        insertUser(conn, "UserName", "password");
        User user = selectUser(conn, "UserName");

        while (true) {
            System.out.println("1. Create to-do item");
            System.out.println("2. Toggle to-do item");
            System.out.println("3. List to-do items");
            System.out.println("4. Delete to-do item");

            String option = scanner.nextLine();

            switch (option) {
                case "1":
                    System.out.println("Enter your to-do item:");
                    String text = scanner.nextLine();

                    insertToDo(conn, user.id, text);
                    break;
                case "2": {
                    System.out.println("Enter the number of the item you want to toggle:");
                    int itemNum = Integer.valueOf(scanner.nextLine());

                    toggleToDo(conn, itemNum);
                    break;
                }
                case "3":

                    ArrayList<ToDoItem> items = selectToDos(conn);
                    for (ToDoItem item : items) {
                        String checkbox = "[ ] ";
                        if (item.isDone) {
                            checkbox = "[x] ";
                        }
                        System.out.println(checkbox + item.id + ". " + item.text);

                    }
                    break;
                case "4": {
                    System.out.println("Enter the number of the item you want to delete.");
                    int itemNum = Integer.valueOf(scanner.nextLine());
                    deleteToDo(conn, itemNum);
                    break;
                }
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}
