package com.db.bankingapi.repositories;

import com.db.bankingapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
