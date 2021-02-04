package com.silkrode.ai.twitter_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twitter4j.TwitterException;

@RestController
@RequestMapping("/api/social_media/twitter/")
public class TwitterController {

    private final TwitterService twitterService;

    public TwitterController(TwitterService twitterService) {
        this.twitterService = twitterService;
    }

    @PostMapping("/search")
    public Object search(@RequestBody TwitterInput twitterInput) throws TwitterException {
        return twitterService.search(twitterInput);
    }

    @PostMapping("/favorite")
    public Object favorite(@RequestBody TwitterInput twitterInput) throws TwitterException {
        return twitterService.favorite(twitterInput);
    }

    @PostMapping("/retweet")
    public Object retweet(@RequestBody TwitterInput twitterInput) throws TwitterException {
        return twitterService.retweet(twitterInput);
    }

    @PostMapping("/reply")
    public Object reply(@RequestBody TwitterInput twitterInput) throws TwitterException {
        return twitterService.reply(twitterInput);
    }

    @PostMapping("/lookup")
    public Object lookup(@RequestBody TwitterInput twitterInput) throws TwitterException {
        return twitterService.lookup(twitterInput);
    }

    @PostMapping("/timeline")
    public Object user(@RequestBody TwitterInput twitterInput) throws TwitterException {
        return twitterService.user(twitterInput);
    }

    @PostMapping("/tweet")
    public ResponseEntity<String> tweet(@RequestBody TwitterInput twitterInput) throws TwitterException{

        return ResponseEntity.ok().body(twitterService.tweet(twitterInput).toString());
    }

    @PostMapping("/autoComment")
    public ResponseEntity<String> autoComment(@RequestBody TwitterInput twitterInput)throws TwitterException{

        return ResponseEntity.ok().body(twitterService.autoReply(twitterInput).toString());
    }

    @PostMapping("/commentReply")
    public ResponseEntity<String> commentReply(@RequestBody TwitterInput twitterInput)throws TwitterException{

        return ResponseEntity.ok().body(twitterService.commentReply(twitterInput).toString());
    }

    @PostMapping("/getReply")
    public Object getReply(@RequestBody TwitterInput twitterInput) throws TwitterException{

        return twitterService.getReply(twitterInput);
    }

}
