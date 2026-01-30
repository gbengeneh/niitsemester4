package com.db.bankingapi.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Individual")
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Individual extends Customer implements Serializable {

    @Column(name="gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name="dob")
    @DateTimeFormat(iso =  DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;



}
