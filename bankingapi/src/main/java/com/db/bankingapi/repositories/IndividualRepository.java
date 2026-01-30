package com.db.bankingapi.repositories;

import com.db.bankingapi.models.Individual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndividualRepository extends JpaRepository<Individual,Long> {
}
