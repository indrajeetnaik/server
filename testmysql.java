package com.mkyong.common;
 
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
 
public class testmysql {
 
  public static void main(String[] argv) {
 
	System.out.println("-------- MySQL JDBC Connection Testing ------------");
 
	try {
		Class.forName("com.mysql.jdbc.Driver");
	} catch (ClassNotFoundException e) {
		System.out.println("Where is your MySQL JDBC Driver?");
		e.printStackTrace();
		return;
	}
 
	System.out.println("MySQL JDBC Driver Registered!");
	Connection connection = null;
	PreparedStatement pst;
	ResultSet rst;
 
	try {
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cloud_data1","root", "root");
 
	} catch (SQLException e) {
		System.out.println("Connection Failed! Check output console");
		e.printStackTrace();
		return;
	}
 
	if (connection != null) {
	
	try{
		System.out.println("You made it, take control your database now!");
		pst=connection.prepareStatement("insert into data_list(group_name,username,file_name,categry,branch)value		('a','c','d','d','e')");
		pst.executeUpdate();
		pst.close();
		connection.close();
}
catch (SQLException e) {
                System.out.println("jhhhhhhhhhhhhhhhhhh");
                e.printStackTrace();
                return;
}
	} else {
		System.out.println("Failed to make connection!");
	}
  }
}

