package sample;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Connection;


public class ConnectionConstruct {
    public static Connection getConnection(){
        Connection connection = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/xuankedb?useTimezone=true&serverTimezone=UTC","root","");

        }catch(Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
