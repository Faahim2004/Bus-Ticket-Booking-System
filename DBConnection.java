package Pjct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	  public static  Connection getconnection() throws SQLException, ClassNotFoundException {
		  
		       Class.forName("com.mysql.cj.jdbc.Driver");
		       String url="jdbc:mysql://localhost:3306/busdb";
		       String user="root";
		       String password="Faahim@2004";
		     return  DriverManager.getConnection(url,user,password);
	  }	   
}