package com.example.authenticationkeycloak.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class TokenUtils {

    private static OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public TokenUtils(ApplicationContext applicationContext) {
        TokenUtils.authorizedClientService = applicationContext.getBean(OAuth2AuthorizedClientService.class);
    }

    public static String extractAccessToken(Authentication authentication) {

        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            String registrationId = oauthToken.getAuthorizedClientRegistrationId();
            String name = oauthToken.getName();

            OAuth2AuthorizedClient client =
                    authorizedClientService.loadAuthorizedClient(registrationId, name);

            if (client != null && client.getAccessToken() != null) {
                return client.getAccessToken().getTokenValue();
            }
        }
        return null;
    }

}
