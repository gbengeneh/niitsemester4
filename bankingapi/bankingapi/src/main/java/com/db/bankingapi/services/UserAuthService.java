package com.db.bankingapi.services;


import com.db.bankingapi.models.Role;
import com.db.bankingapi.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAuthService implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user=this.userService.getUserByName(userName);
        if(user==null){

            throw new UsernameNotFoundException("User Not Existing");
        }else{

            List<Role> roles=userService.getRoles(userName);
            List<GrantedAuthority> grantedAuthorities = roles.stream().map(r -> {
                return new SimpleGrantedAuthority(r.getRoleName());
            }).collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(),
                    grantedAuthorities);

        }
    }
}
