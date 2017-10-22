package com.Model;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterHandle {
	
	private static final String AUTH_CONSUMER_KEY = <AUTH_CONSUMER_KEY>;
	private static final String AUTH_CONSUMER_SECRET = <AUTH_CONSUMER_SECRET>;
	private static final String AUTH_ACCESS_TOKEN = <AUTH_ACCESS_TOKEN>;
	private static final String AUTH_ACCESS_TOKEN_SECRET = <AUTH_ACCESS_TOKEN_SECRET>;
	static Configuration conf;
	static
    {
        ConfigurationBuilder cb;
        cb=new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(AUTH_CONSUMER_KEY)
            .setOAuthConsumerSecret(AUTH_CONSUMER_SECRET)
            .setOAuthAccessToken(AUTH_ACCESS_TOKEN)
            .setOAuthAccessTokenSecret(AUTH_ACCESS_TOKEN_SECRET)
                ;
        conf=cb.build();
    }//static
	 public static TwitterStream twitterStream = new TwitterStreamFactory(conf).getInstance();
}
