package edu.sjsu.cmpe275.aop.aspect;

import java.util.*;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import edu.sjsu.cmpe275.aop.TweetStatsImpl;

@Aspect
@Order(0)
public class StatsAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */

    @Autowired
    TweetStatsImpl stats;
    @Autowired
    RetryAspect retry;

    @Before("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..))")
    public void BeforeTweet(JoinPoint joinPoint) {
        String tweetMsg = (String) joinPoint.getArgs()[1];
        stats.checkTweet(tweetMsg);
        System.out.println("--------------------------------------------------------------------------------------");
    }

    @AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.tweet(..))")
    public void AfterTweet(JoinPoint joinPoint) {
        if (retry.flag == 0) {
            System.out.printf("in tweet");
            String user = (String) joinPoint.getArgs()[0];
            String tweetMsg = (String) joinPoint.getArgs()[1];
            if (user.length() == 0) {
                throw new IllegalArgumentException("User cannot be null");
            } else if (tweetMsg.length() > 140) {
                throw new IllegalArgumentException("Message cannot be more than 140 characters");
            } else if (tweetMsg.length() == 0) {
                throw new IllegalArgumentException("Message can not be null");
            } else {
                stats.saveTweet(user, tweetMsg);
            }
        } else {
            retry.flag = 0;
        }
        //stats.resetStats();
        System.out.println("--------------------------------------------------------------------------------------");
    }


    @AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.follow(..))")
    public void BeforeFollow(JoinPoint joinPoint) {
        if (retry.flag == 0) {
            System.out.printf("Before the execution of the Follow method -%s\n", joinPoint.getSignature().getName());
            String follower = (String) joinPoint.getArgs()[0];
            String followee = (String) joinPoint.getArgs()[1];
            System.out.println(follower + " trying to follow " + followee);
            boolean blockedCheck = stats.checkIfBlocked(follower, followee);
            System.out.println("Is user is blocked ????? - " + blockedCheck);
            boolean followCheck = stats.checkIfFollowed(follower, followee);
            System.out.println("Is user is already present in follower list ????? - " + followCheck);

            if (followee != follower) {
                if (followee == "" || follower == "") {
                    System.out.println("Follower/Followee can not be null !!!");
                    throw new IllegalArgumentException("Follower/Followee can not be null !!!");
                } else if (blockedCheck == false) {
                    if (followCheck == false) {
                        stats.saveFollow(follower, followee);
                        stats.displayLists();
                    } else {
                        System.out.println("Follower is already in Follower List.. Can not perform this operation !!");
                    }
                } else {
                    System.out.println("Follower is in Blocked List.. Can not perform this operation !!");
                }
            } else {
                System.out.println("User cannot follow himself/herself !!!");
                throw new IllegalArgumentException("User cannot follow himself/herself !!!");
            }
        } else {
            retry.flag = 0;
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }

    @AfterReturning("execution(public void edu.sjsu.cmpe275.aop.TweetService.block(..))")
    public void BeforeBlock(JoinPoint joinPoint) {
        if (retry.flag == 0) {
            System.out.printf("Before the execution of the Block method %s\n", joinPoint.getSignature().getName());
            String user = (String) joinPoint.getArgs()[0];
            String followee = (String) joinPoint.getArgs()[1];

            System.out.println(user + " trying to block " + followee);

            if (user != followee) {
                if (followee == "" || user == "") {
                    System.out.println("user/Followee can not be null !!!");
                } else if (stats.checkIfFollowed(user, followee)) {
                    stats.updateFollowList(user, followee);
                    stats.saveBlock(user, followee);
                    stats.displayLists();
                } else {
                    stats.saveBlock(user, followee);
                    stats.displayLists();
                }
            } else {
                System.out.println("User cannot block himself/herself !!!");
            }
        } else {
            retry.flag = 0;
        }
        System.out.println("--------------------------------------------------------------------------------------");
    }
}
