package com.silkrode.ai.twitter_api;


import com.github.houbb.opencc4j.core.ZhConvert;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import twitter4j.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.stereotype.Service
public class TwitterService {

    TwitterConfing twitterConfing = new TwitterConfing();
    TwitterFactory twitterFactory = new TwitterFactory(twitterConfing.configbuilderPoster());
    TwitterFactory twitterFactoryReply = new TwitterFactory(twitterConfing.configbuilderReplier());

    public Object search(TwitterInput twitterInput){
        try {
            Twitter twitter = twitterFactory.getInstance();
            String retweet = twitterInput.isInclude_retweet() ? "" : "\" +exclude:retweets\"";
            int count = twitterInput.getCount() != 0 ? twitterInput.getCount() : 15;
            Long max_id = twitterInput.getMax_id() == null ? Long.MAX_VALUE : Long.valueOf(twitterInput.getMax_id());
            String resultType = twitterInput.getResult_type() == null ? "mixed" : twitterInput.result_type;
            StringBuffer keyword = new StringBuffer();
            for (String s : twitterInput.getKeyword())
                keyword.append(ZhConverterUtil.toSimple(s));
            Query query = new Query(keyword.toString() + retweet);
            query.setCount(count);
            query.setResultType(Query.ResultType.valueOf(resultType));
            query.setMaxId(max_id);
            query.lang(twitterInput.getLanguage());
            QueryResult result = twitter.search(query);
            TwitterOutput twitterOutput = new TwitterOutput();
            List<Map<String, Object>> resultList = new ArrayList<>();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            for (Status status : result.getTweets()) {
                Map m = new HashMap();
                m.put("date_time", sdFormat.format(status.getCreatedAt()));
                m.put("id", String.valueOf(status.getId()));
                m.put("content", status.getText());
                m.put("user", status.getUser().getName());
                m.put("screen_name", status.getUser().getScreenName());
                m.put("retweet_count", status.getRetweetCount());
                m.put("favorite_count", status.getFavoriteCount());
                m.put("link", "https://twitter.com/" + status.getUser().getScreenName() + "/status/" + status.getId());
                resultList.add(m);
            }
            twitterOutput.set_result(resultList);
            return twitterOutput;
        }catch (TwitterException e){
            Error error = new Error();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
    }

    public Object favorite(TwitterInput twitterInput) {
        try {
            Twitter twitter = twitterFactory.getInstance();
            TwitterOutput twitterOutput = new TwitterOutput();
            if (twitterInput.operation.equals("create")) {
                twitter.createFavorite(Long.valueOf(twitterInput.status_id));
                twitterOutput.setOk(true);
                twitterOutput.setResult("favorite created");
            } else {
                twitter.destroyFavorite(Long.valueOf(twitterInput.status_id));
                twitterOutput.setOk(true);
                twitterOutput.setResult("favorite destroyed");
            }
            return twitterOutput;
        }catch (TwitterException e){
            Error error = new Error();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
    }

    public Object retweet(TwitterInput twitterInput)  {
        try {
            Twitter twitter = twitterFactory.getInstance();
            TwitterOutput twitterOutput = new TwitterOutput();
            if (twitterInput.operation.equals("create")) {
                Long id = twitter.retweetStatus(Long.valueOf(twitterInput.status_id)).getId();
                twitterOutput.setOk(true);
                twitterOutput.setResult("retweet created");
                twitterOutput.setId(String.valueOf(id));
            } else {
                twitter.unRetweetStatus(Long.valueOf(twitterInput.status_id));
                twitterOutput.setOk(true);
                twitterOutput.setResult("retweet destroyed");
            }
            return twitterOutput;
        } catch (TwitterException e) {
            Error error = new Error();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
    }

    public Object reply(TwitterInput twitterInput) {
        try {
            Twitter twitter = twitterFactory.getInstance();
            TwitterOutput twitterOutput = new TwitterOutput();
            if (twitterInput.operation.equals("create")) {
                Status status = twitter.showStatus(Long.valueOf(twitterInput.getStatus_id()));
                Status reply = twitter.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + twitterInput.getComment()).inReplyToStatusId(Long.valueOf(twitterInput.getStatus_id())));

                Long id = reply.getId();
                twitterOutput.setOk(true);
                twitterOutput.setResult("reply created");
                twitterOutput.setId(String.valueOf(id));
            } else {
                twitter.destroyStatus(Long.valueOf(twitterInput.status_id));
                twitterOutput.setOk(true);
                twitterOutput.setResult("reply destroyed");
            }
            return twitterOutput;
        }catch (TwitterException e){
            Error error = new Error();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
    }

    public Object lookup(TwitterInput twitterInput) {
        try {
            Twitter twitter = twitterFactory.getInstance();
            return twitter.showStatus(Long.valueOf(twitterInput.status_id));
        }catch (TwitterException e){
            Error error = new Error();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
    }

    public Object user(TwitterInput twitterInput) {
        try {
            Twitter twitter = twitterFactory.getInstance();
            String id = twitterInput.getUser_id() == null ? "@gencomments32" : twitterInput.getUser_id();
            int limit = twitterInput.getLimit() == 0 ? 100 : twitterInput.getLimit();
            int skip = twitterInput.getSkip() == 0 ? 0 : twitterInput.getSkip();
            int pageSize = 200;
            Paging page = new Paging();
            page.setCount(pageSize);
            ResponseList<Status> status = twitter.getUserTimeline(id, page);

            TwitterOutput twitterOutput = new TwitterOutput();
            List<Map<String, Object>> tempResultList = new ArrayList<>();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            for (Status s : status) {
                Map m = new HashMap();
                m.put("created_at", s.getCreatedAt());
                m.put("id", String.valueOf(s.getId()));
                m.put("text", s.getText());
                m.put("link", "https://twitter.com/" + s.getUser().getScreenName() + "/status/" + s.getId());
                if (s.getInReplyToStatusId() == -1)
                    m.put("reply_to", null);
                else
                    m.put("reply_to", String.valueOf(s.getInReplyToStatusId()));
                m.put("user_id", String.valueOf(s.getUser().getId()));
                m.put("user_name", s.getUser().getName());
                if (s.getRetweetedStatus() != null) {
                    m.put("type", "retweet");
                    m.put("retweeted_id", String.valueOf(s.getRetweetedStatus().getId()));
                    m.put("retweeted_screen_name", s.getRetweetedStatus().getUser().getScreenName());
                } else if (s.getInReplyToStatusId() > 0)
                    m.put("type", "reply");
                else
                    m.put("type", "post");
                tempResultList.add(m);
            }

            page.setCount(pageSize);
            ResponseList<Status> favorite = twitter.getFavorites(id, page);
            for (Status s : favorite) {
                Map m = new HashMap();
                m.put("created_at", s.getCreatedAt());
                m.put("id", String.valueOf(s.getId()));
                m.put("text", s.getText());
                m.put("link", "https://twitter.com/" + s.getUser().getScreenName() + "/status/" + s.getId());
                if (s.getInReplyToStatusId() == -1)
                    m.put("reply_to", null);
                else
                    m.put("reply_to", s.getInReplyToStatusId());
                m.put("user_id", String.valueOf(s.getUser().getId()));
                m.put("user_name", s.getUser().getName());
                m.put("type", "favorite");
                tempResultList.add(m);
            }
            Collections.sort(tempResultList, (o1, o2) -> {
                Date d1 = (Date) o1.get("created_at");
                Date d2 = (Date) o2.get("created_at");
                return d1.compareTo(d2);
            });
            Collections.reverse(tempResultList);
            List<Map<String, Object>> resultList = new ArrayList<>();
            if (skip < tempResultList.size()) {
                for (int i = skip; i < tempResultList.size(); i++) {
                    if (i >= limit+skip)
                        break;
                    Map m = tempResultList.get(i);
                    m.put("created_at", sdFormat.format(m.get("created_at")));
                    resultList.add(m);
                }
            }
            twitterOutput.set_result(resultList);
            return twitterOutput;
        }catch (TwitterException e){
            Error error = new Error();
            error.setErrorCode(e.getErrorCode());
            error.setErrorMessage(e.getErrorMessage());
            return ResponseEntity.badRequest().body(error.toString());
        }
    }

    public Object tweet(TwitterInput twitterInput) throws TwitterException {
        Timestamp time = new Timestamp(System.currentTimeMillis());
        Twitter twitter = twitterFactory.getInstance();
        String s = time.toString().split(" ")[0] + "-" +
                twitterInput.tweetContent + "\n";
        StatusUpdate statusUpdate = new StatusUpdate(s);
        String tweetIDAndContent = twitter.updateStatus(statusUpdate).getId() + "&&" + twitterInput.tweetContent.split("\n")[1];
        return tweetIDAndContent;
    }

    public Object autoReply(TwitterInput twitterInput) throws TwitterException {

        Twitter twitter = twitterFactoryReply.getInstance();
        Status status = twitter.showStatus(Long.valueOf(twitterInput.getStatus_id()));
        Status reply = twitter.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + twitterInput.getComment()).inReplyToStatusId(Long.valueOf(twitterInput.getStatus_id())));
        return reply.getText();
    }

    public Object commentReply(TwitterInput twitterInput) throws TwitterException {

        Twitter twitter = twitterFactory.getInstance();
        Status status = twitter.showStatus(Long.valueOf(twitterInput.getStatus_id()));
        Status reply = twitter.updateStatus(new StatusUpdate("@" + status.getUser().getScreenName() + " " + twitterInput.getComment()).inReplyToStatusId(Long.valueOf(twitterInput.getStatus_id())));
        return reply.getText();
    }

    public Object getReply(TwitterInput twitterInput) throws TwitterException {
        Twitter twitter = twitterFactory.getInstance();
        List<String> content = new ArrayList<>();
        String queryString = "@gencomments32";
        Query query = new Query(queryString);
        QueryResult result = twitter.search(query);
        List<Status> tweets = result.getTweets();
        for (Status s : tweets) {
            content.add(s.getText().replace("@gencomments32 ", "") + "&&" + s.getId());
        }
        return content;
    }

}
