package com.example.stortorget.service;

import com.example.stortorget.entity.SalesAd;
import com.example.stortorget.entity.User;
import com.example.stortorget.repository.SalesAdRepository;
import com.example.stortorget.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SalesAdRepository salesAdRepository;

    public void saveUser(User user){

        String hashedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPass);

        userRepository.save(user);
    }

    public User getUserByUserName(String userName){

        return userRepository.findByUserName(userName);
    }

    public void deleteUser(User user){

        userRepository.delete(user);
    }

    public User getCurrentUser(Principal principal){

        return userRepository.findByUserName(principal.getName());

    }

    public void editUser(User user, Principal principal) {

        user.setId(userRepository.findByUserName(principal.getName()).getId());
        user.setUserName(principal.getName());

        // Om inget nytt lösenord har angavs så sätts användarens tidigare lösenord
        // else - så hashas det nya lösenordet
        if(user.getPassword().equals("")){
            user.setPassword(userRepository.findByUserName(principal.getName()).getPassword());
        }
        else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.save(user);
    }

    public List<SalesAd> getUserAds(Principal principal){

        return salesAdRepository.findByUserName(principal.getName());

    }

}
