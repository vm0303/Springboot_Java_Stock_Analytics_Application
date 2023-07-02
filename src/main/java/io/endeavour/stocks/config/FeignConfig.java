package io.endeavour.stocks.config;

import feign.RequestInterceptor;
import io.endeavour.stocks.service.LoginService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


@Configuration
public class FeignConfig {

    private LoginService loginService;

    public FeignConfig(LoginService loginService) {
        this.loginService = loginService;
    }

    @Bean
    public RequestInterceptor getRequestInterceptor()
    {
        return request -> request.header(HttpHeaders.AUTHORIZATION, "Bearer " + loginService.getBearerToken());
    }
}
