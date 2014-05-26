package connection;




import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kitty
 */
public class MysqlConnection
{


  // static String url = "jdbc:mysql://mysql3000.mochahost.com/";
    static String url = "jdbc:mysql://localhost:3306/";
    //static String db = "raghav23_registration";
    static String db = "privacy";
    static String driver = "com.mysql.jdbc.Driver";
    //static String username="raghav23",password="kitty23";
    static String username="root",password="mysql";
    public Connection connection;
    
    public Connection establishConnection()
    {
        try
        {
         Class.forName(driver);
         connection=DriverManager.getConnection(url+db,username,password);
         return connection;
        }
        catch(Exception e)
        {
            System.out.println("Error in Mysql Connection Class"+e.getLocalizedMessage());
            return null;
        }
    }

    public static Connection getMysqlConnection() throws Exception
    {

       
      
        Connection connection=new MysqlConnection().establishConnection();

        return connection;
    }

}
