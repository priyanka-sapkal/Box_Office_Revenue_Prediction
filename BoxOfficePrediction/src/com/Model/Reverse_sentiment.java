package com.Model;

import java.util.Arrays;
import rita.wordnet.RiWordnet;

public class Reverse_sentiment 
{
	
	public static boolean useList(String[] arr, String targetValue) 
	{
		return Arrays.asList(arr).contains(targetValue);
	}

	String reverse(String tweettobereversed) 
	{

		String[] result = tweettobereversed.split("\\s");
		String ans ="";
		RiWordnet wordnet = new RiWordnet(null);
		for(int k =0;k<result.length; k++)//awesome,movie
		{
			
			String[] partsofspeech = wordnet.getPos(result[k]);//a
			boolean adj=useList(partsofspeech,"a");
			
			
			if(adj)
			{
				if(k!=0)//if not 1st word
				{
					if(result[k-1].equals("not") || result[k-1].equals("don't") || result[k-1].equals("do not") )
						
					{
								ans = ans.replace(result[k-1], "");
								ans+= result[k]+" "; 
								
					}
					
					else
					{

						try
						{
							String[] antonyms = wordnet.getAllAntonyms(result[k],"a");	
						    ans+= antonyms[0]+" ";
						}
						catch(Exception e)
						{
							ans+= result[k]+" ";//e.printStackTrace();
						}
					}
				}//if not 1st word
				else
				{
					try
					{
						String[] antonyms = wordnet.getAllAntonyms(result[k],"a");		
						ans+= antonyms[0]+" ";
					}
					catch(Exception e)
					{
						//e.printStackTrace();
					}
				}//else
			}//if adjective
			else
			{
				ans+= result[k]+" ";
			}
				
				
			}//for
			
		
		return ans;
		
		
	
	}
}


