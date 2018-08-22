package io.github.daniloarcidiacono.stackoverflow.springsessionthreadsafety.controller;

import org.apache.tomcat.util.codec.binary.Base64;

public abstract class SecurityUtils {
    public static String basicAuthentication(final String username, final String password) {
        final String plainCreds = username + ":" + password;
        return new String(Base64.encodeBase64(plainCreds.getBytes()));
    }
}
