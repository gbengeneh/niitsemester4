package com.db.bankingapi.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private RestClient restClient;
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/v1.0")
    public ResponseEntity<?> getUsers(){
        String response=restClient.get().uri("https://jsonplaceholder.typicode.com/users")
                .retrieve()
                .body(String.class);
        logger.info(response);
        logger.info(LocalDate.now().toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
