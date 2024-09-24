package TSOA_Quatitative_Test;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class DB {

    Connection conn;

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/tsoa", "root", "abc@123");
            System.out.println("Connection is created successfully:");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(String data) {
        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(data);
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable(String name, String columns){
        update("CREATE TABLE "+name+" ("
                + "id INT(64) NOT NULL AUTO_INCREMENT,"
                + "aname VARCHAR(20),"
                + "fname VARCHAR(20),"
                + columns
                + ",itr INT(64),"
                + "PRIMARY KEY ( id ))");
    }

    public PreparedStatement getStatement(String name, String columns, String dels) throws SQLException {
        String sql = "INSERT INTO "+name+" (aname, fname,"+columns+",itr) VALUES (?,?,"+dels+",?)";
        return conn.prepareStatement(sql);
    }

    public PreparedStatement getStatement(
            String name,
            String columns,
            String dels,
            String aname,
            String fname,
            double value) throws SQLException {
        String sql = "INSERT INTO "+name+" (aname, fname,"+columns+",itr) VALUES (?,?,"+dels+",?)";
        return conn.prepareStatement(sql);
    }

    @Test
    public void createTables(){
        connect();
        createTable("best_value","data FLOAT");
        createTable("final_results","mean_val FLOAT, max_val FLOAT, min_val FLOAT, std_val FLOAT");
        close();
    }

    public void addFinal(
            String aname,
            String fname,
            double mean,
            double max,
            double min,
            double std,
            int itr) throws SQLException {
        String sql = "INSERT INTO final_results (aname, fname,mean_val,max_val,min_val,std_val,itr) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, aname);
        p.setString(2, fname);
        p.setDouble(3, mean);
        p.setDouble(4, max);
        p.setDouble(5, min);
        p.setDouble(6, std);
        p.setInt(7, itr);

        execute(p);
    }

    public void addDouble(
            String name,
            String columns,
            String aname,
            String fname,
            double value,
            int itr) throws SQLException {
        String sql = "INSERT INTO "+name+" (aname, fname,"+columns+",itr) VALUES (?,?,?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, aname);
        p.setString(2, fname);
        p.setDouble(3, value);
        p.setInt(4, itr);

        execute(p);
    }

    public void addDString(
            String name,
            String columns,
            String aname,
            String fname,
            String value,
            int itr) throws SQLException {
        String sql = "INSERT INTO "+name+" (aname, fname,"+columns+",itr) VALUES (?,?,?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, aname);
        p.setString(2, fname);
        p.setString(3, value);
        p.setInt(4, itr);

        execute(p);
    }

    public String createString(String s, int n){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n;i++){
            sb.append(s);
        }
        return sb.toString();
    }

    public void execute(PreparedStatement pr) throws SQLException {
        pr.executeUpdate();
        pr.close();
    }
}
