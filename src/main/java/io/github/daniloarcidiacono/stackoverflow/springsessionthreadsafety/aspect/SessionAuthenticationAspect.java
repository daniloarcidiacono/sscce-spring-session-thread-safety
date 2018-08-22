package io.github.daniloarcidiacono.stackoverflow.springsessionthreadsafety.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.stereotype.Component;

/**
 * The session authentication strategy chain, and in particular {@link ConcurrentSessionControlAuthenticationStrategy} and
 * {@link RegisterSessionAuthenticationStrategy}, is not thread-safe. This aspect forces the synchronization.
 */
@Aspect
@Component
@ConditionalOnProperty(name="peak.force-sync", havingValue = "true")
public class SessionAuthenticationAspect {
    @Around("execution(* org.springframework.security.web.authentication.session.CompositeSessionAuthenticationStrategy.onAuthentication(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        synchronized (this) {
            joinPoint.proceed();
        }
    }
}
