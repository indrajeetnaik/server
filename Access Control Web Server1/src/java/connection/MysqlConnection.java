package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class MysqlConnection
{


    static String url = "jdbc:mysql://localhost:3306/";
    static String db = "cloud_data1";
    static String driver = "com.mysql.jdbc.Driver";
    static String username="root",password="divya";
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
