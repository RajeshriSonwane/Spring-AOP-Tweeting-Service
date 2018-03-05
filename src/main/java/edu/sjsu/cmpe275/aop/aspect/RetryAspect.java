package edu.sjsu.cmpe275.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Around;

import java.io.IOException;

@Aspect
@Order(1)
public class RetryAspect {
    /***
     * Following is a dummy implementation of this aspect.
     * You are expected to provide an actual implementation based on the requirements, including adding/removing advices as needed.
     */
    int flag=0;

    @Around("execution(public void edu.sjsu.cmpe275.aop.TweetService.*(..))")
    public void dummyAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        //System.out.printf("Prior to the executuion of the metohd %s\n", joinPoint.getSignature().getName());

        try {
            joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal Exception " + e.toString());
        } catch (IOException i1) {
            System.out.println("Network Failure.. - Attempt 1 ");
            try {
                joinPoint.proceed();
            } catch (Throwable t1) {
                System.out.println("Network Failure..- Attempt 2");
                try {
                    joinPoint.proceed();
                } catch (Throwable t2) {
                    System.out.println("Network Failure.. - Attempt 3");
                    try {
                        joinPoint.proceed();
                    } catch (Throwable t3) {
                        System.out.println("Network Error Can not do action try again later");
                        flag=1;
                    }
                }
            }
        }
    }
}
