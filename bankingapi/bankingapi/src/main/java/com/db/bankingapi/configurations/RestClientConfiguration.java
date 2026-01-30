package com.db.bankingapi.configurations;

import com.db.bankingapi.models.Address;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient createBean() {
       return RestClient.create();

    }



}
