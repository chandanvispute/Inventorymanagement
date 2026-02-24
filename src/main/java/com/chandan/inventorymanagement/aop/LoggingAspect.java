    package com.chandan.inventorymanagement.aop;
    
    import org.aspectj.lang.ProceedingJoinPoint;
    import org.aspectj.lang.annotation.Around;
    import org.aspectj.lang.annotation.Aspect;
    import org.springframework.stereotype.Component;
    
    @Aspect
    @Component
    public class LoggingAspect {
    
        @Around("execution(* com.chandan.inventorymanagement.service..*(..))")
        public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    
            long start = System.currentTimeMillis();
    
            Object result = joinPoint.proceed(); // execute method
    
            long end = System.currentTimeMillis();
    
            System.out.println(
                    "Method: " + joinPoint.getSignature() +
                            " | Execution Time: " + (end - start) + " ms"
            );
    
            return result;
        }
    }
