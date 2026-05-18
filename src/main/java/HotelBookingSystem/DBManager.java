package HotelBookingSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBManager {

    private static final String URL = "jdbc:derby:HotelBookingSystemDB_Ebd;create=true";

    Connection conn;

    public DBManager() {
        establishConnection();
    }

    /**
     * Connects to embedded Derby, creates tables if needed, and seeds ROOMS table data.
     */
    public static DBManager startup() {
        DBManager db = new DBManager();
        Connection connection = db.getConnection();
        if (connection == null) {
            throw new IllegalStateException("Could not connect to Derby: " + URL);
        }
        new DatabaseInitializer(connection).initialize();
        new DataBaseStorage(connection).seed();
        return db;
    }

    public static void main(String[] args) {
        DBManager dbManager = startup();
        System.out.println(dbManager.getConnection());
        try {
            System.out.println(dbManager.getConnection().getSchema());
        } catch (SQLException ex) {
            System.getLogger(DBManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void establishConnection() {
        if (this.conn == null) {
            try {
                conn = DriverManager.getConnection(URL);
                System.out.println(URL + " Get Connected Successfully ....");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
