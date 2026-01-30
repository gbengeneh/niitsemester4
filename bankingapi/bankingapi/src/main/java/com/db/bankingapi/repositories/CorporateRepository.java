package com.db.bankingapi.repositories;

import com.db.bankingapi.models.Corporate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CorporateRepository extends JpaRepository<Corporate,Long> {
}
