package com.StockAppBackend.fullstackbackend.service.map;



import com.StockAppBackend.fullstackbackend.dto.LoginDTO;
import com.StockAppBackend.fullstackbackend.entity.User;
import com.StockAppBackend.fullstackbackend.exception.UserNotFoundException;
import com.StockAppBackend.fullstackbackend.repo.UserRepo;
import com.StockAppBackend.fullstackbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User loginUser(LoginDTO loginDTO){

        User user = null;
        try{
            user = userRepo.findByUsername(loginDTO.getUsername());
        }catch (Exception e){
            e.printStackTrace();
        }

        String pass = loginDTO.getPassword();
        if(user != null && passwordEncoder.matches(pass, user.getPassword())){
            return user;
        }
        return null;
    }

    public User createEmployee(User user){
        String userName = user.getUsername();

        if(userRepo.findByUsername(userName) != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user exist!");
        }


        if (user.getUsername() == null || user.getUsername().isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }else{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }


        return userRepo.save(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = userRepo.findAll();
        return users;
    }

    public List<User> findByStatus(String status) {
        List<User> users = userRepo.findByStatus(status);
        return users;
    }


    @Override
    public User findById(Long aLong) {
        return userRepo.findById(aLong).orElseThrow(()->new UserNotFoundException(aLong));
    }

    @Override
    public User save(User object) {


        User user = null;

        try{
            user = userRepo.findByUsername(object.getUsername());
        }catch (Exception e){
            e.printStackTrace();
        }

        if(user == null){
            return userRepo.save(object);
        }
        return null;
    }

    @Override
    public void delete(User object) {
        userRepo.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        userRepo.deleteById(aLong);
    }


    public User update(User newUser, Long id){
        return userRepo.findById(id).map(user -> {
            user.setUsername(newUser.getUsername());
            user.setEmail(newUser.getEmail());
            user.setPassword(newUser.getPassword());

            user.setName(newUser.getName());
            user.setSurname(newUser.getSurname());
            user.setPatronymic(newUser.getPatronymic());
            user.setStatus(newUser.getStatus());

            return userRepo.save(user);
        }).orElseThrow(()->new UserNotFoundException(id));
    }
}
