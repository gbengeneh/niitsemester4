package com.db.bankingapi.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Corporate")
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class Corporate extends Customer implements Serializable {
    @Enumerated(EnumType.STRING)
    @Column(name="company_type")
    private CompanyType companyType;


}
