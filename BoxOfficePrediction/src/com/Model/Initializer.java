package com.Model;

import java.sql.*;

public class Initializer
{
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/project";
   public static Connection inConn1;
   public static Connection inConn2;
  
  Statement stmt = null;
   
   //  Database credentials
   static final String USER = <username>;
   static final String PASS = <password>;
   
		   public  void connection_open() throws ClassNotFoundException
		   {
		 
			   try
			   {
				   Class.forName(JDBC_DRIVER);
				   System.out.println("Connecting to database...");
				   	inConn1 = DriverManager.getConnection(DB_URL,USER,PASS);
					inConn2 = DriverManager.getConnection(DB_URL,USER,PASS);
				   	System.out.println("Creating statement...");
				   	stmt = inConn1.createStatement();
				   	
				  
		       }
			   catch(SQLException se)
			   {
				   se.printStackTrace();
			   }
		   
			   finally
			   {
				   try
				   {
					   if(stmt!=null)
						  stmt.close();
		           }
		           catch(SQLException se2)
		           {
		           }
		      
		      
		   }
		}
		   
		public  void connection_close() throws ClassNotFoundException, SQLException
	   {
		  inConn1.close(); 
	   }
   
  
}





