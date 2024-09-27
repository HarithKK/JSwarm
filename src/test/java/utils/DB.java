package utils;

import org.junit.jupiter.api.Test;
import org.usa.soc.si.SIAlgorithm;
import org.usa.soc.util.Commons;
import org.usa.soc.util.Validator;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class DB {

    private static DB instance;

    public static DB getInstance(){
        if(instance == null)
        {
            instance = new DB();
            instance.connect();
        }
        return instance;
    }

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

    public void createTable(String name, String columns) throws SQLException {
        if(tableExist(conn, name)==false){
            update("CREATE TABLE "+name+" ("
                    + "id INT(64) NOT NULL AUTO_INCREMENT,"
                    + "testid VARCHAR(250),"
                    + "aname VARCHAR(250),"
                    + "fname VARCHAR(250),"
                    + columns
                    + ",itr INT(64),"
                    + "PRIMARY KEY ( id ))");
        }else{
            System.out.println("Tables have already bean created");
        }
    }

    // from stackoverflow
    // https://stackoverflow.com/questions/2942788/check-if-table-exists
    public static boolean tableExist(Connection conn, String tableName) throws SQLException {
        boolean tExists = false;
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(tableName)) {
                    tExists = true;
                    break;
                }
            }
        }
        return tExists;
    }
    @Test
    public void createTables(){
        try{
            connect();
            update("CREATE TABLE test_info (id INT(64) NOT NULL AUTO_INCREMENT,testid VARCHAR(250),test_description VARCHAR(250),execution_date VARCHAR(250), PRIMARY KEY ( id ))");
            createTable("best_value", "data double");
            createTable("final_results", "mean_val double, max_val double, min_val double, std_val double, p_value double");
            createTable("max_values", "tsoa VARCHAR(250),ssa VARCHAR(250),mfa VARCHAR(250),cso VARCHAR(250),pso VARCHAR(250),also VARCHAR(250),ba VARCHAR(250),avoa VARCHAR(250),tsa VARCHAR(250),gwo VARCHAR(250)");
            createTable("min_values", "tsoa VARCHAR(250),ssa VARCHAR(250),mfa VARCHAR(250),cso VARCHAR(250),pso VARCHAR(250),also VARCHAR(250),ba VARCHAR(250),avoa VARCHAR(250),tsa VARCHAR(250),gwo VARCHAR(250)");
            createTable("mean_values", "tsoa VARCHAR(250),ssa VARCHAR(250),mfa VARCHAR(250),cso VARCHAR(250),pso VARCHAR(250),also VARCHAR(250),ba VARCHAR(250),avoa VARCHAR(250),tsa VARCHAR(250),gwo VARCHAR(250)");
            createTable("std_values", "tsoa VARCHAR(250),ssa VARCHAR(250),mfa VARCHAR(250),cso VARCHAR(250),pso VARCHAR(250),also VARCHAR(250),ba VARCHAR(250),avoa VARCHAR(250),tsa VARCHAR(250),gwo VARCHAR(250)");
            createTable("p_values", "tsoa VARCHAR(250),ssa VARCHAR(250),mfa VARCHAR(250),cso VARCHAR(250),pso VARCHAR(250),also VARCHAR(250),ba VARCHAR(250),avoa VARCHAR(250),tsa VARCHAR(250),gwo VARCHAR(250)");
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close();
        };
    }

    public void addData(String testid,
                        String aname,
                        String fname,
                        double data,
                        int itr)throws SQLException{
        String sql = "INSERT INTO best_value (testid, aname,fname,data,itr) VALUES (?,?,?,?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, testid);
        p.setString(2, aname);
        p.setString(3, fname);
        p.setDouble(4, Validator.validateDouble(data));
        p.setInt(5, itr);

        execute(p);
    }

    public void addTestInfo(String testid, String description)throws SQLException{
        String sql = "INSERT INTO test_info (testid, execution_date) VALUES (?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, testid);
        p.setString(2, description);
        p.setString(3, new Date().toString());

        execute(p);
    }

    public void addFinal(
            String aname,
            String fname,
            String uuid,
            double mean,
            double max,
            double min,
            double std,
            double pValue,
            int itr) throws SQLException {
        String sql = "INSERT INTO final_results (testid,aname, fname,mean_val,max_val,min_val,std_val,p_value,itr) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, uuid);
        p.setString(2, aname);
        p.setString(3, fname);
        p.setDouble(4, Validator.validateDouble(mean));
        p.setDouble(5, Validator.validateDouble(max));
        p.setDouble(6, Validator.validateDouble(min));
        p.setDouble(7, Validator.validateDouble(std));
        p.setDouble(8, Validator.validateDouble(pValue));
        p.setInt(9, itr);

        execute(p);
    }

    private Double fixMax(List<Double> i){
        if(i.size() > 0){
            return i.stream().mapToDouble(d -> (Double)d).max().getAsDouble();
        }
        return null;
    }

    private Double fixMin(List<Double> i){
        if(i.size() > 0){
            return i.stream().mapToDouble(d -> (Double)d).min().getAsDouble();
        }
        return null;
    }

    private Double fixStd(List<Double> i){
        if(i.size() > 0){
            return Utils.calcStd(i);
        }
        return null;
    }

    private Double fixMean(List<Double> i){
        if(i.size() > 0){
            return i.stream().mapToDouble(d -> (Double)d).average().getAsDouble();
        }
        return null;
    }

    public void addFinal(
            String aname,
            String fname,
            String uuid,
            List<Double>[] algos,
            SIAlgorithm algorithm,
            int itr) throws SQLException {
        String sql = "INSERT INTO max_values (testid,aname,fname,tsoa,ssa,mfa,cso,pso,also,ba,avoa,tsa,gwo,itr) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement p = conn.prepareStatement(sql);
        p.setString(1, uuid);
        p.setString(2, aname);
        p.setString(3, fname);
        for(int i=0; i<=9;i++){
            p.setString(i+4, String.valueOf(fixMax(algos[i])));
        }
        p.setInt(14, itr);
        execute(p);

        sql = "INSERT INTO min_values (testid,aname,fname,tsoa,ssa,mfa,cso,pso,also,ba,avoa,tsa,gwo,itr) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        p = conn.prepareStatement(sql);
        p.setString(1, uuid);
        p.setString(2, aname);
        p.setString(3, fname);
        for(int i=0; i<=9;i++){
            p.setString(i+4, String.valueOf(fixMin(algos[i])));
        }
        p.setInt(14, itr);
        execute(p);

        sql = "INSERT INTO mean_values (testid,aname,fname,tsoa,ssa,mfa,cso,pso,also,ba,avoa,tsa,gwo,itr) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        p = conn.prepareStatement(sql);
        p.setString(1, uuid);
        p.setString(2, aname);
        p.setString(3, fname);
        for(int i=0; i<=9;i++){
            p.setString(i+4, String.valueOf(fixMean(algos[i])));
        }
        p.setInt(14, itr);
        execute(p);

        sql = "INSERT INTO std_values (testid,aname,fname,tsoa,ssa,mfa,cso,pso,also,ba,avoa,tsa,gwo,itr) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        p = conn.prepareStatement(sql);
        p.setString(1, uuid);
        p.setString(2, aname);
        p.setString(3, fname);
        for(int i=0; i<=9;i++){
            p.setString(i+4, String.valueOf(fixStd(algos[i])));
        }
        p.setInt(14, itr);
        execute(p);

        sql = "INSERT INTO p_values (testid,aname,fname,tsoa,ssa,mfa,cso,pso,also,ba,avoa,tsa,gwo,itr) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        p = conn.prepareStatement(sql);
        p.setString(1, uuid);
        p.setString(2, aname);
        p.setString(3, fname);
        for(int i=0; i<=9;i++){
            if(algos[i].size() == 0) {
                p.setString(i + 4, "NULL");
                continue;
            }
            p.setString(i+4, String.valueOf(Commons.calculatePValue(algos[i].stream().mapToDouble(t->t).toArray(), Commons.fill(algorithm.getObjectiveFunction().getExpectedBestValue(), algos.length))));
        }
        p.setInt(14, itr);
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
        p.setDouble(3, Validator.validateDouble(value));
        p.setInt(4, itr);

        execute(p);
    }

    public void execute(PreparedStatement pr) throws SQLException {
        pr.executeUpdate();
        pr.close();
    }
}
