/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xchatbox_v06;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Nahid
 */
public class DBConnect {

    private Connection con;
    private Statement st;
    private ResultSet rs;

    public DBConnect() {


        String host = "jdbc:mysql://localhost:3306/xChat";
        String username = "root";
        String password = "";

        /*
        String host = "jdbc:mysql://www.xchat.eu5.org:3306/447252";
        String username = "447252";
        String password = "10201001";
         */
        /*
        String host = "jdbc:mysql://localhost/447252";
        String username = "447252";
        String password = "10201001";
         */
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //con = DriverManager.getConnection("jdbc:mysql://xchat.eu5.org:3306/447252", "447252", "10201001");
            //con = DriverManager.getConnection("jdbc:mysql://localhost/xChat", "root", "");
            con = DriverManager.getConnection(host, username, password);
            st = con.createStatement();
            System.out.println("Connected to the Database.");

        } catch (Exception ex) {
            System.out.println("DB_ERROR: " + ex);
        }
    }

    public String[] getData() {
        String[] array = new String[100];
        int index_array = 0;
        String all = null;
        try {
            String query = "select * from userlist";
            rs = st.executeQuery(query);
            System.out.println("Records From Database");

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("userName");
                String time = rs.getString("startTime");
                all = " Name: " + name + " Time: " + time;
                System.out.println("ID: " + id + " Name: " + name + " Time: " + time);
                array[index_array] = name;
                index_array++;
            }
            return array;
        } catch (Exception ex) {
            System.out.println("getData_ERROR: " + ex);
        }
        return array;
    }

    public void Insert(String name) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date1 = new Date();
            final String date_text_val = dateFormat.format(date1).toString();


            PreparedStatement statement = (PreparedStatement) con.prepareStatement("INSERT INTO userlist(id,userName,startTime)VALUES(?,?,?);");
            statement.setString(1, null);
            statement.setString(2, name);
            statement.setString(3, date_text_val);
            statement.executeUpdate();
            statement.close();
            con.close();
            System.out.println("Insert Entry DONE");
        } catch (Exception ex) {
            System.out.println("Insert_ERROR: " + ex);
        }

    }

    public void Delete(String name) {

        try {

            String query = "DELETE FROM userlist WHERE userName = ? ";
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Delete_ERROR: " + e);
        }

    }

    public void DeleteAll() {

        try {
            st = con.createStatement();
            String query = "DELETE FROM userlist";
            int deletedRows = st.executeUpdate(query);
            System.out.println("DONE DELETINGALL");
        } catch (Exception e) {
            System.out.println("DeleteALL_ERROR: " + e);
        }

    }

    public String[] getTime() {
        String[] array = new String[100];
        int index_array = 0;
       
        try {
            String query = "select * from userlist";
            rs = st.executeQuery(query);
            System.out.println("Records From Database");

            while (rs.next()) {
                String time = rs.getString("startTime");                
                array[index_array] = time;
                index_array++;
            }
            return array;
        } catch (Exception ex) {
            System.out.println("getTime_ERROR: " + ex);
        }
        return array;
    }
}
