package com.db.bankingapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.regex.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Customer")
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
public class Customer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="account_no")
    protected long accountNo;
    @Embedded
    protected FullName  fullName;
    @Column(name="email", unique=true, nullable=false,length = 150)
    protected String email;
    @Column(name="password", nullable=false,length = 10)
    protected String password;
    @Column(name="contact_no")
    protected long contactNo;
}
