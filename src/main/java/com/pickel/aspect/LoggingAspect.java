package com.pickel.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Pointcut("within(com.pickles.controller..*) || " +
              "within(com.pickles.service..*) || " +
              "within(com.pickles.repository..*)")
    public void applicationPackagePointcut() {}
    
    @Before("applicationPackagePointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}",
            joinPoint.getSignature().toShortString(),
            Arrays.toString(joinPoint.getArgs()));
    }
    
    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Method {} executed successfully. Return value: {}",
            joinPoint.getSignature().toShortString(),
            result);
    }
    
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method: {} with message: {}",
            joinPoint.getSignature().toShortString(),
            exception.getMessage());
    }
    
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            
            log.info("Method {} executed in {} ms",
                joinPoint.getSignature().toShortString(),
                executionTime);
            
            return result;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - start;
            log.error("Method {} failed after {} ms with exception: {}",
                joinPoint.getSignature().toShortString(),
                executionTime,
                e.getMessage());
            throw e;
        }
    }
}