package edu.sjsu.cmpe275.aop;

import java.util.*;

public class TweetStatsImpl implements TweetStats {
    /***
     * Following is a dummy implementation.
     * You are expected to provide an actual implementation based on the requirements.
     */
    HashMap<String, List<String>> followersList = new HashMap<String, List<String>>();
    HashMap<String, List<String>> blockedList = new HashMap<String, List<String>>();
    HashMap<String, List<String>> tweets = new HashMap<String, List<String>>();

    private int lengthOfLongestTweetAttempted = 0, mostFollowedUserCount = 0, mostBlockedUserCount = 0, totalTweetLength = 0;
    String mostFollowedUser = "", mostProductiveUser = "", mostBlockedFollower = "";

    @Override
    public void resetStatsAndSystem() {
        // TODO Auto-generated method stub
        lengthOfLongestTweetAttempted = 0;
        mostFollowedUser = "";
        mostProductiveUser = "";
        mostBlockedFollower = "";
        followersList.clear();
        blockedList.clear();
        tweets.clear();
    }

    @Override
    public int getLengthOfLongestTweetAttempted() {
        // TODO Auto-generated method stub
        return lengthOfLongestTweetAttempted;
    }

    @Override
    public String getMostFollowedUser() {
        // TODO Auto-generated method stub
        for (Map.Entry<String, List<String>> me : followersList.entrySet()) {
            String key = me.getKey();
            List<String> valueList = me.getValue();

            if (mostFollowedUserCount < followersList.get(key).size() && followersList.get(key).size() != 0) {
                mostFollowedUserCount = followersList.get(key).size();
                mostFollowedUser = key;
            } else if (mostFollowedUserCount == followersList.get(key).size() && followersList.get(key).size() != 0) {
                if (mostFollowedUser.compareTo(key) > 0) {
                    mostFollowedUser = key;
                }
            }
        }
        return mostFollowedUser;
    }

    @Override
    public String getMostProductiveUser() {
        // TODO Auto-generated method stub
        for (Map.Entry<String, List<String>> pu : tweets.entrySet()) {
            int maxLength = 0;
            String key = pu.getKey();
            List<String> valueList = pu.getValue();
            for (String s : valueList) {
                maxLength = maxLength + s.length();
            }
            if (maxLength > totalTweetLength) {
                totalTweetLength = maxLength;
                mostProductiveUser = key;
            }else if(maxLength == totalTweetLength){
                if(mostProductiveUser.compareTo(key)>0){
                    mostProductiveUser = key;
                }
            }
        }
        return mostProductiveUser;
    }

    @Override
    public String getMostBlockedFollower() {
        // TODO Auto-generated method stub
        for (Map.Entry<String, List<String>> bl : blockedList.entrySet()) {
            String key = bl.getKey();
            if (mostBlockedUserCount < blockedList.get(key).size() && blockedList.get(key).size() != 0) {
                mostBlockedUserCount = blockedList.get(key).size();
                mostBlockedFollower = key;
            } else if (mostBlockedUserCount == blockedList.get(key).size()) {
                if (mostBlockedFollower.compareTo(key) > 0) {
                    mostBlockedFollower = key;
                }
            }
        }
        return mostBlockedFollower;
    }


    public void checkTweet(String message) {
        if (lengthOfLongestTweetAttempted < message.length()) {
            lengthOfLongestTweetAttempted = message.length();
        }
    }

    public void saveTweet(String user, String message) {
        if (lengthOfLongestTweetAttempted < message.length()) {
            lengthOfLongestTweetAttempted = message.length();
        }
        if (!tweets.containsKey(user)) {
            tweets.put(user, new ArrayList<String>());
        }
        tweets.get(user).add(message);
        System.out.println("Tweeted Data Saved!!!");
    }

    public void saveFollow(String follower, String followee) {
        if (!followersList.containsKey(followee)) {
            followersList.put(followee, new ArrayList<String>());
        }
        followersList.get(followee).add(follower);
        System.out.println(follower + " followed " + followee + " Successfully !! Follow Data Saved!!!");
    }

    public void saveBlock(String user, String followee) {
        if (!blockedList.containsKey(followee)) {
            blockedList.put(followee, new ArrayList<String>());
        }
        blockedList.get(followee).add(user);
        System.out.println("Blocked Data Saved!!!");
    }

    public boolean checkIfBlocked(String user, String followee) {
        for (Map.Entry<String, List<String>> bl : blockedList.entrySet()) {
            String key = bl.getKey();
            if (key == user) {
                List<String> valueList = bl.getValue();
                for (String s : valueList) {
                    if (s == followee) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void displayLists() {
        System.out.println("Blocked List -");
        for (Map.Entry<String, List<String>> bl : blockedList.entrySet()) {
            String key = bl.getKey();
            System.out.println(key);
            List<String> valueList = bl.getValue();
            for (String s : valueList) {
                System.out.println(" - " + s);
            }
        }
        System.out.println("Follow List -");
        for (Map.Entry<String, List<String>> fl : followersList.entrySet()) {
            String key = fl.getKey();
            System.out.println(key);
            List<String> valueList = fl.getValue();
            for (String s : valueList) {
                System.out.println(" - " + s);
            }
        }
    }

    public void updateFollowList(String user, String followee) {
        followersList.get(followee).remove(user);
        System.out.println("Follower List Updated!!!");
    }

    public boolean checkIfFollowed(String user, String followee) {
        for (Map.Entry<String, List<String>> fl : followersList.entrySet()) {
            String key = fl.getKey();
            if (key == followee) {
                List<String> valueList = fl.getValue();
                for (String s : valueList) {
                    if (s == user) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}