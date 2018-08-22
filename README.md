# SCCE for Spring Session thread safety
This repository is a [SSCCE](http://sscce.org/) for testing the thread safety of Spring Session, in particular relative to concurrent session management.

Libraries used:
* Spring Boot 2.0.4.RELEASE
* Spring Session
* Spring Security
* Spring AOP
* JUnit and MockMVC for integration tests
 
## Issue description
`SessionManagementFilter` does not seem to be thread-safe against concurrent login requests.
Given the following configuration:

```java
   http
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
                .sessionRegistry(sessionRegistry)
                .maxSessionsPreventsLogin(true)
            .and()
        .and()
```

the `maximumSession` constraint is not enforced when multiple concurrent requests are performed; this scenario is simulated by `PeakTest`.

A possible workaround is implemented with `SessionAuthenticationAspect` aspect, that wraps `CompositeSessionAuthenticationStrategy.onAuthentication`
with a `synchronized` block.