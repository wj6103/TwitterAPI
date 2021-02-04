package com.silkrode.ai.twitter_api;

import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

@org.springframework.context.annotation.Configuration
public class TwitterConfing {

    String posterConsumerKey = "rUVkPSQQXyxc2tfwDde921CiA";
    String posterConsumerSecret = "xVnFXn03XKprB2DyhK6tEVKNXPnmoCxBEMP64MJoAakOWhU4Nl";
    String posterAccessToken = "1287682374336040960-rG4CAqUCqPMCBxP5DCTyVRAujbvmC8";
    String posterAccessTokenSecret = "2qJEG6s7kutGpcSTo3Ar5FX6xhMI3J3N4po9odTJjDO5w";

    String replierConsumerKey = "N514CBemLsn0zRqqHAJBg2A2G";
    String replierConsumerSecret = "0964CtWUodK9JOQ90dvjAXUQquRSZHnVaFEcXmyEw5Yc74BFIW";
    String replierAccessToken = "1243911532473274368-4M2jXVdRMvCrerv3WvHw4gYIR9f9mW";
    String replierAccessTokenSecret = "HGNemPsW5EhFSlfkW0rHn3OJIDmgVW8WitOrwj6DHEg6t";


    public Configuration configbuilderPoster(){
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(this.posterConsumerKey)
                .setOAuthConsumerSecret(this.posterConsumerSecret)
                .setOAuthAccessToken(this.posterAccessToken)
                .setOAuthAccessTokenSecret(this.posterAccessTokenSecret);
        return configurationBuilder.build();
    }

    public Configuration configbuilderReplier(){
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(this.replierConsumerKey)
                .setOAuthConsumerSecret(this.replierConsumerSecret)
                .setOAuthAccessToken(this.replierAccessToken)
                .setOAuthAccessTokenSecret(this.replierAccessTokenSecret);
        return configurationBuilder.build();
    }

}
