package com.funsoft.magazin.Securité.Service;


import com.funsoft.magazin.Repository.UserRepository;
import com.funsoft.magazin.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository agent;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        User user = agent.findByEmail(username).orElseThrow(()->
                new UsernameNotFoundException("User not found with this address email : "+username));
        return UserDetailsImpl.build(user);
    }
}
