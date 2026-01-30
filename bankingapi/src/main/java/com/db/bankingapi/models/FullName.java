package com.db.bankingapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class FullName {
    @Column(name="first_name", nullable=false,length = 100)
    private String firstName;
    @Column(name="last_name", nullable=false,length = 100)
    private String lastName;
    @Column(name="middle_name", nullable=true,length = 100)
    private String middleName;
    //if first name not in alphabets

}
