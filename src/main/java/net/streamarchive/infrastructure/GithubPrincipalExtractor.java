package net.streamarchive.infrastructure;

import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

public class GithubPrincipalExtractor
        implements PrincipalExtractor {

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        if (!map.get("login").equals("pinginno")) {
            throw new BadCredentialsException("");
        }
        return map.get("login");
    }


}