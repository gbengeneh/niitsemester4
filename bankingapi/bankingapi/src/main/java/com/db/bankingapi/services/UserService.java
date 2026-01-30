package com.db.bankingapi.services;



import com.db.bankingapi.models.Role;
import com.db.bankingapi.models.User;

import java.util.List;

public interface UserService {

    User addUser(User user);
    List<User> getAllUsers();

    List<Role> getRoles(String userName);
    User getUserByName(String userName);

}
