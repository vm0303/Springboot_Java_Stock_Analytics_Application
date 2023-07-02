package io.endeavour.stocks.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoginService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);
    private String loginURL;
    private RestTemplate restTemplate;

    public LoginService(@Value("${client.stock-calculations.url}") String baseUrl, @Value("${client.login.username}") String userName,
                        @Value("${client.login.password}") String password)
    {
            loginURL = baseUrl + "/login";

            restTemplate = new RestTemplate();
            //This added interceptor below(i.e BasicAuthenticationInterceptor) adds Authorization header
            //for every request going to the LoginService with Basic <encoded value of id and password>
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName,password));

    }

    /**
     * Method to fire a POST Webservice call to the Login Service and get the Token in the response body
     * @return String JWT token
     */
    public String getBearerToken()
    {
     LOGGER.info("Getting Bearer JWT Token from the Login Service");
        ResponseEntity<String> responseEntity = restTemplate.exchange(loginURL, HttpMethod.POST, null, String.class);
        return responseEntity.getBody();
    }
}
