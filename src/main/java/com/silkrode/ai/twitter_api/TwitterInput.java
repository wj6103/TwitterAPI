package com.silkrode.ai.twitter_api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TwitterInput {

    String user_id;
    @JsonProperty("id")
    String status_id;
    String name;
    String screen_name;
    String[] keyword;
    String result_type;
    String language;
    String content;
    String comment;
    String date_time;
    String operation;
    String max_id;
    int count;
    int retweet_count;
    int favorite_count;
    boolean include_retweet;
    String tweetContent;
    int limit;
    int skip;

}
