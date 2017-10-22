package com.Model;

import rita.wordnet.RiWordnet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.*;
public class Clean_The_Data 
{
	
	
	private final static String REGEX = "((www\\.[\\s]+)|(https?://[^\\s]+))";
	
	private final static String STARTS_WITH_NUMBER = "[1-9]\\s*(\\w*)";
	static RiWordnet wordnet = new RiWordnet(null);
	static Stemmer obj=new Stemmer();
	
	 String clean(String re,String actor,String actress,String moviename) throws ParseException
	 {
		 String tweet=""; 
		 String actorregex = actor.trim().replaceAll(" +", "");							//remove multiple spaces
		 String actressregex = actress.trim().replaceAll(" +", "");						//remove multiple spaces
		 String movieregex = moviename.trim().replaceAll(" +", "");						//remove multiple spaces
   
	/*************************************************************************HASH TAGS AND URLS***************************************************************************************/
	 
			
		 re = re.replaceAll(REGEX, "");												//remove url
		 re=re.replaceAll("[-+.^:,!?()/[/]]","");									//remove special characters
		 re = re.replaceAll("-", " ");												//remove -
		 re= re.replaceAll(STARTS_WITH_NUMBER, "");
		 re = re.trim().replaceAll(" +", " ");										//remove multiple spaces
			 
		// System.out.println("After special symbols removal : \n"+re+"\n \n");
		 
		 /********************************************************************PREPOSITIONS*************************************************************************************/
		 List<String> list = new ArrayList<String>();
		 list.addAll(Arrays.asList(actor,actress,moviename,actorregex,actressregex,movieregex,"you","i","this","that","of","for","to","the","with","where","there","here","from","my","your","her","and","his"));
		 
			 String[] result = re.split("\\s");
			 
			 int length=result.length;
			 int[] inDict=new int[length];
			 for(int k =0;k<length; k++)
			 {
			 inDict[k]=1;
			 }																//initializing inDict
	
			 for(int k =0;k<length; k++)
			 {
					 //System.out.println(result[k]);
					 if(result.length>1 &&(result[k].charAt(0)=='#' || result[k].charAt(0)=='@'))
					 {
						 	result[k]=result[k].substring(1);
						
					 }
					 else
					 {
						 if(list.contains(result[k]))
						 		continue;
						 	else
						 	{
						 		String[] partsofspeech = wordnet.getPos(result[k]);														//updating inDict
						 		if(partsofspeech.length==0)
						 		{
						 			inDict[k]=0;
						 		}
						 	}
					 }//else
					
			 }//for of hash
			
	 /********************************************************************EXTENDED WORDS*************************************************************************************/
		 for(int k =0;k<length; k++)
		 {	
				 if(inDict[k]==0)
				 {
		
						 int len=result[k].length();
						 for(int i=len-1;i>=1;i--)
						 	{
							 		if(result[k].charAt(i)==result[k].charAt(i-1)) //repeated
							 		{
												String[] partsofspeech = wordnet.getPos(result[k]);
												if(partsofspeech.length==0)														//not in dictionary
												{
														result[k]=result[k].substring(0,i)+result[k].substring(i+1);
														len=result[k].length();
														String[] partsofspeech1 = wordnet.getPos(result[k]);
														if(partsofspeech1.length>0)	
														{
															inDict[k]=1;
															break;
														}
														
															
													
												}
												
										
							 		}// outer if
						 	}// inner for
				 }//end of indict if
		 }// outer for
		
	 
	 /********************************************************************SHORT FORM REMOVAL*************************************************************************************/
		 for(int i=0;i<length;i++)
		 {
		 
		// URBAN DICT
			 if(inDict[i]==0)
			 {
			 	try
			 	{
						
						 HttpResponse<JsonNode> response = Unirest.get("https://mashape-community-urban-dictionary.p.mashape.com/define")
						 .header("X-Mashape-Key", "RkD1Bqe4iDmshIbZt6ILBxwDXetKp1TsdQIjsnPPDBw8fIgPzR")
						 .header("Accept", "text/plain")
						 .queryString("term",result[i])
						 .asJson();
						
						  String s1 = response.getBody().toString();
						
						
						JSONParser parser = new JSONParser();
						Object obj = parser.parse(s1);
						JSONObject jsonObject = (JSONObject) obj;
						JSONArray array =  (JSONArray) jsonObject.get("tags");
						try
						{
								String correctedterm=(String) array.get(0);
								result[i]=correctedterm;
								inDict[i]=1;
								
						}
						catch(Exception e)
						{}
				
		
			 	} 
			 	catch (UnirestException e1) 
			 	{
						// TODO Auto-generated catch block
						e1.printStackTrace();
			 	}
		 }//if
	 }//for
		
	 
	 /********************************************************************SPELL CHECK*************************************************************************************/
	//   SPELL CHECK
	 for(int i=0;i<length;i++)
	 {
		 if(inDict[i]==0)
		 {

			 	try 
			 	{
						  HttpResponse<JsonNode> response = Unirest.post("https://montanaflynn-spellcheck.p.mashape.com/check/")
						 .header("X-Mashape-Key", "RkD1Bqe4iDmshIbZt6ILBxwDXetKp1TsdQIjsnPPDBw8fIgPzR")
						 .header("Accept", "application/json")
						 .queryString("text",result[i])
						 .asJson();
						String s = response.getBody().toString();
						JSONParser parser = new JSONParser();
						Object obj = parser.parse(s);
						JSONObject jsonObject = (JSONObject) obj;
						try
						{
								String correctTweet =(String)jsonObject.get("suggestion");
								inDict[i]=1;
								result[i]= correctTweet;
						}
						catch(Exception e)
						{}
				
			 	} 
			 	catch (UnirestException e)
			 	{
					// TODO Auto-generated catch block
			 		e.printStackTrace();
			 	} 
			 	catch (ParseException e) 
			 	{
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
		 }//if
	 }//for of spell check 
	
	 
	 
	 /************************************************************************STEMMING******************************************************************************************/
	 
	   tweet = Arrays.toString(result);
	   tweet=obj.StemText(tweet,inDict);
	   tweet=tweet.substring(1,tweet.length()-1);
	   return tweet;
	 
}//main 
}

