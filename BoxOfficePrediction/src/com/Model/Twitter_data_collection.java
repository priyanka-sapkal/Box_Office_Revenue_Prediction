package com.Model;

import static com.Model.Initializer.inConn1;
import static com.Model.TwitterHandle.twitterStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;


public class Twitter_data_collection
{ 
	
		 static int count=0;
		 Statement stmt;
		 long startTime,stopTime,elapsedTime;
		 static Initializer in=new Initializer();
		 PreparedStatement pstmt;
      
		 public void data_collect(final String title, String hashtag) throws ClassNotFoundException
	     {
	    	 in.connection_open();
	         startTime=System.currentTimeMillis(); 
	         StatusListener listener = new StatusListener() 
	         {
	        	 public void onStatus(Status arg0) 
		         {
		        	
		 	        		Status rt= arg0.getRetweetedStatus();
		 					if(rt==null)
		 					{
		 				    	java.util.Date d;
		 				    	d = arg0.getCreatedAt();
		 				        String username = arg0.getUser().getScreenName();
		 			            count++;
		 	                    try 
		 	                    {
		 	                    	   String sql = "SELECT MOVIE_ID FROM movie where TITLE = ?";
		 	                    	   pstmt = inConn1.prepareStatement(sql);
			 	                  	   pstmt.setString(1, title);
			 	                  	   ResultSet rs1=pstmt.executeQuery();
			 	                       rs1.next();
			 	               	       int Id=rs1.getInt(1); 
			 	               	
		 	                    	   
			 	                       pstmt = inConn1.prepareStatement("INSERT INTO tweet(OriginalText,CreatedAt,User_name,MOVIE_ID)  VALUES (?,?,?,?,?)");
			 	                       pstmt.setString(1, arg0.getText() );
			 	                  	   pstmt.setTimestamp(2, new java.sql.Timestamp(d.getTime()));
			 	                  	   pstmt.setString(3, username);
			 	                  	   pstmt.setInt(4, Id);
			 	                  	   pstmt.executeUpdate();
		 	                      } 
		 	                      catch (SQLException e) 
		 	                      {
		 								// TODO Auto-generated catch block
		 								e.printStackTrace();
		 	                      }
		 	                      System.out.println("Total number of tweets captured : "+count);
		 	                      
		 	                }//if of retweet
		 					
		 					stopTime=System.currentTimeMillis();
		 					elapsedTime=stopTime-startTime;
		 					if(elapsedTime>120000)
	 	                      {
	 	                    	
	 	                    	 JOptionPane.showMessageDialog(null, "Shutting Down....\n Data collection done!\n You can start Processing now.");
	 	                    	 twitterStream.shutdown();
	 	                    	 try 
	 	                    	 {
									in.connection_close();
								 } 
	 	                    	 catch (SQLException e) 
	 	                    	 {
									// TODO Auto-generated catch block
									e.printStackTrace();
								 } 
	 	                    	 catch (ClassNotFoundException e) 
	 	                    	 {
									// TODO Auto-generated catch block
									e.printStackTrace();
								 }
	 	                   	
	 	                   	 }//if of time
		 	     }//onstatus
	        	

				@Override
				public void onException(Exception arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onDeletionNotice(StatusDeletionNotice arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onScrubGeo(long arg0, long arg1) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onStallWarning(StallWarning arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTrackLimitationNotice(int arg0) {
					// TODO Auto-generated method stub
					
				}
		          
	        };
	        FilterQuery fq = new FilterQuery();
	        String keywords[] = {hashtag};				//which hashtags to follow, movie website
	        fq.track(keywords);
	        twitterStream.addListener(listener);
	        twitterStream.filter(fq);
	       
	       }
     
 }


		

	

