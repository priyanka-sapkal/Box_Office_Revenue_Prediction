package com.Model;


import static com.Model.Initializer.inConn1;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.json.simple.JSONObject;


public class Processing_Starts
{ 
    static Double alpha=0.6;
	static Statement stmt;
	static Initializer in=new Initializer();
   	static PolarityAnalyzer pol=new PolarityAnalyzer();
    static  Reverse_sentiment rev=new Reverse_sentiment();
    static Clean_The_Data ctd=new Clean_The_Data();
    
    public void starts(String title)throws ClassNotFoundException, SQLException
	    {
	    	in.connection_open();															//connect to database
	    	int num=0;
	    	int id = 0;
	    	PreparedStatement pstmt;
	    	ResultSet rs1;
	    	
	        System.out.println("Processing started\n");										
	        String SQL1 = "SELECT MOVIE_ID FROM movie where TITLE = ?";						//get id of the movie from movie name
      	    pstmt = inConn1.prepareStatement(SQL1);
        	pstmt.setString(1, title);
        	rs1=pstmt.executeQuery();
            rs1.next();
     	    int MovieId=rs1.getInt(1); 
     	   
		    
	     	pstmt = inConn1.prepareStatement("Select MIN(Id) from tweet where MOVIE_ID=? and Processed=?");	
     	pstmt.setInt(1, MovieId);													//get id of the tweet from which processing has to be started
	     	pstmt.setInt(2, 0);
	        rs1=pstmt.executeQuery();
	        rs1.next();
	      	int MinId=rs1.getInt(1); 
     	   
     	   
			pstmt = inConn1.prepareStatement("Select ActorName,ActressName from movie where MOVIE_ID=?");										//get total number of tweets
	   	    pstmt.setInt(1,MovieId);
	   	    rs1=pstmt.executeQuery();
	   	    rs1.next();
	   	    String actor=rs1.getString(1);
		    String actress=rs1.getString(2);
		    
		  
		    pstmt = inConn1.prepareStatement("create view temp as Select * from tweet where MOVIE_ID=? and Id>=?");				
	   	    pstmt.setInt(1,MovieId);
	   	    pstmt.setInt(2,MinId);
	   	    pstmt.executeUpdate();
	   	    
	   	    pstmt = inConn1.prepareStatement("SELECT Id,OriginalText FROM temp");	
	   	    ResultSet rs = pstmt.executeQuery();
    	   
    	    while(rs.next())
    	    {
    	    	try
    	    	{
    	        
    	    		            String FinalSentiment;
				    	        Double FinalHowPositive=null,FinalHowNegative = null,FinalConfidence=null;
				    	        String tweet = rs.getString("OriginalText");
				    	        id = rs.getInt("Id");
				    	        System.out.println("tweet : "+tweet);
    	    					tweet=ctd.clean(tweet,actor,actress,title);
		 			            JSONObject jo = null,rjo = null;
		 			            jo=pol.getSentiment(tweet);
		 			            String sent =(String) jo.get("type");
		 			            System.out.println("Sentiment"+sent);
		 					    Double conf =(Double) jo.get("score");
		 					   
		 				    	conf=Math.abs(conf);
		 				    	System.out.println("conf : "+conf);
		 						String reversed=rev.reverse(tweet);
		 			            rjo=pol.getSentiment(reversed);
		 						String rsent =(String) rjo.get("type");
		 						System.out.println("RSentiment"+rsent);
		 			            Double rconf =(Double) rjo.get("score");
		 				    	rconf=Math.abs(rconf);
		 				    	System.out.println("rconf : "+rconf);
		 			               
		 			               
		 				    	
		 				    	   
	/************************************************************************** DUAL PREDICTION*******************************************************/	
		 				    	   
		 				    	   if(sent.equals("positive") && rsent.equals("positive"))						
		 				    	 {
		 				    	   FinalHowPositive = ((1-alpha)*conf)+(alpha*(1-rconf));
		 				    	   FinalHowNegative = ((1-alpha)*(1-conf))+(alpha*rconf);
		 				    	 }
		 				    	 
		 				    	 else if(sent.equals("negative") && rsent.equals("negative"))
		 				    	 {
		 				    	   FinalHowPositive = ((1-alpha)*(1-conf))+(alpha*rconf);
		 				    	   FinalHowNegative = ((1-alpha)*conf)+(alpha*(1-rconf));
		 				    	 }
		 				    	 
		 				    	 else if(sent.equals("positive") && rsent.equals("negative"))
		 				    	 {
		 				    	   FinalHowPositive = ((1-alpha)*conf)+(alpha*rconf);
		 				    	   FinalHowNegative = ((1-alpha)*(1-conf))+(alpha*(1-rconf));
		 				    	 }
		 				    	 
		 				    	 else if(sent.equals("negative") && rsent.equals("positive"))
		 				    	 {
		 				    	   FinalHowPositive = ((1-alpha)*(1-conf))+(alpha*(1-rconf));
		 				    	   FinalHowNegative = ((1-alpha)*conf)+(alpha*rconf);
		 				    	 }
		 				    	else 
		 				    	 {
		 				    		FinalHowPositive=FinalHowNegative=conf;
		 				    	 }
		 				    	 
		 				    	  System.out.println("FinalHowPositive : "+FinalHowPositive);
		 				    	 if(FinalHowPositive>FinalHowNegative)
		 				    	 {
		 				    		 FinalSentiment="positive";
		 				    		 FinalConfidence=FinalHowPositive;
		 				    	 }
		 				    	else if(FinalHowPositive==FinalHowNegative)
		 				    	 {
		 				    		 FinalSentiment=sent;
		 				    		FinalConfidence=conf;
		 				    	 }
		 				    	 else
		 				    	 {
		 				    		 FinalSentiment="negative";
		 				    		FinalConfidence=FinalHowNegative;
		 				    	 }
		 				    	 System.out.println(" FinalSentiment : "+ FinalSentiment);
		 	/********************************************************************************************************************************************************/	 				   
		 				    	  
			 	                 pstmt = inConn1.prepareStatement("Update temp SET Text=?,Sentiment=?,Confidence=?,ReverseText=?,Rsentiment=?,Rconfidence=?,FinalHowPositive=?,FinalHowNegative=?,FinalSentiment=?,FinalConfidence=?,Processed=?  where Id=?");
			 	                 pstmt.setString(1, tweet );
			 	                 pstmt.setString(2, sent);
			 	                 pstmt.setDouble(3, conf);
			 	             	 pstmt.setString(4, reversed );
			 	             	 pstmt.setString(5, rsent);
			 	             	 pstmt.setDouble(6, rconf);
			 	             	 pstmt.setDouble(7, FinalHowPositive);
			 	             	 pstmt.setDouble(8, FinalHowNegative);
			 	             	 pstmt.setString(9, FinalSentiment );
			 	             	 pstmt.setDouble(10, FinalConfidence);
			 	                 pstmt.setInt(11, 1);
				 	             pstmt.setLong(12, id);  
				 	             pstmt.executeUpdate();
		 	             	  
		 	                    num++;
		 	                    System.out.println("Number of tweets processed : "+num);
		 	                     
    	      }//try
    	    
    	    catch(Exception e)
    	    {
    	    	//e.printStackTrace();
    	    	 pstmt = inConn1.prepareStatement("DELETE from temp where ID=?");					//delete tweets which could not be processed
    	    	 stmt = inConn1.createStatement();
    	    	 pstmt.setInt(1,id);
    	    	 pstmt.executeUpdate();
    	    	
    	    }
    	    }//while
    	    
    	     
     	
    	    
    	    int PositiveTweets,NegativeTweets;
		 	double PolarityRatio,Hype,RateOfTweets=0,TotalTweets,DistinctUsers;																					
		    pstmt = inConn1.prepareStatement("Select count(*) from tweet where Sentiment=? and MOVIE_ID=?");					//get number of positive tweets
         	pstmt.setString(1,"positive");
         	pstmt.setInt(2,MovieId);
            rs1=pstmt.executeQuery();
            rs1.next();
        	PositiveTweets=rs1.getInt(1); 
       	    System.out.println("positive tweets : \n"+PositiveTweets); 
       	    
       	    
       	    pstmt=inConn1.prepareStatement("Select count(*) from tweet where Sentiment=? and MOVIE_ID=?");										//get number of negative tweets
     	    pstmt.setString(1,"negative");
     	    pstmt.setInt(2,MovieId);
     	    rs1=pstmt.executeQuery();
     	    rs1.next();
   	        NegativeTweets=rs1.getInt(1);
      	    System.out.println("negative tweets : \n"+NegativeTweets); 
      	    
      	    if(NegativeTweets==0)
      	    	PolarityRatio=100;	
      	    else
      	    	PolarityRatio=PositiveTweets/NegativeTweets;
      	    
      	    System.out.println("ratio :"+PolarityRatio); 
      	    
      	    pstmt = inConn1.prepareStatement("Select count(distinct User_name) from tweet where MOVIE_ID=?");						//get number of distinct users who tweeted
      	    pstmt.setInt(1,MovieId);
      	    rs1=pstmt.executeQuery();
      	    rs1.next();
 	        DistinctUsers=rs1.getDouble(1);
 	        
 	        
      	    pstmt = inConn1.prepareStatement("Select count(*) from tweet where MOVIE_ID=?");										//get total number of tweets
      	    pstmt.setInt(1,MovieId);
      	    rs1=pstmt.executeQuery();
      	    rs1.next();
 	        TotalTweets=rs1.getDouble(1);
 	        System.out.println("total tweets:" +TotalTweets+" distinct : "+DistinctUsers);
 	        
 	        Hype=(double)(DistinctUsers/TotalTweets)+RateOfTweets;
 	        System.out.println("hype:" +Hype);
 	        
 	        
      	    pstmt=inConn1.prepareStatement("Update movie set PolarityRatio=?,Hype=? where TITLE=?");					//update values
      	    pstmt.setDouble(1,PolarityRatio);
      	    pstmt.setDouble(2,Hype);
      	    pstmt.setString(3,title);
      	    pstmt.executeUpdate();
      	    
      	    pstmt=inConn1.prepareStatement("drop view temp");					//drop the view 
            pstmt.executeUpdate();
      	    
       	    in.connection_close();												//close database connection
       	    
       	    Regression reg = new Regression();									//call regression
       	    reg.regression(MovieId);
	        
	       }//starts
       
	    }//class


		

	

