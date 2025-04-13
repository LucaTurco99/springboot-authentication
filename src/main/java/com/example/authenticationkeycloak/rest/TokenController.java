package com.example.authenticationkeycloak.rest;

import com.example.authenticationkeycloak.utils.TokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class TokenController {

    @GetMapping("/token")
    @ResponseBody
    public ResponseEntity<?> getToken(Authentication authentication) {
        String token = TokenUtils.extractAccessToken(authentication);
        if (token != null) {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Bearer " + token);
                HttpEntity<String> entity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.github.com/user",
                    HttpMethod.GET,
                    entity,
                    String.class
                );

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("access_token", token);
                    jsonObject.put("info_user", new ObjectMapper().readValue(response.getBody(), JSONObject.class));
                    jsonObject.put("api", "https://api.github.com/");
                    return new ResponseEntity<>(jsonObject, HttpStatus.OK);
                } catch (JsonProcessingException e) {
                    return new ResponseEntity<>("Error processing JSON", HttpStatus.INTERNAL_SERVER_ERROR);
                }
        }
        return new ResponseEntity<>("Token not found", HttpStatus.UNAUTHORIZED);
    }
}
