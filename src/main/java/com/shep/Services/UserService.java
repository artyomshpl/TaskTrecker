package com.shep.Services;

import com.shep.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    public com.shep.Entities.User saveUser(com.shep.Entities.User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<String> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
    }

    public com.shep.Entities.User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
