package com.db.bankingapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Address")
public class Address {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="address_id")
    private long addressId;
    @Column(name="door_no", nullable=false,length = 5)
    private String doorNo;
    @Column(name="street_name", nullable=false,length = 150)
    private String streetName;
    @Column(name="city", nullable=false,length = 100)
    private String city;
    @Column(name="state", nullable=false,length = 100)
    private String state;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(foreignKey = @ForeignKey(name = "account_no"),name="account_no_fk")
    private Customer  customer;

}
