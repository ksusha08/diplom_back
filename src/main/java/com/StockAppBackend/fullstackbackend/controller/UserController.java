package com.StockAppBackend.fullstackbackend.controller;


import com.StockAppBackend.fullstackbackend.dto.LoginDTO;
import com.StockAppBackend.fullstackbackend.entity.User;
import com.StockAppBackend.fullstackbackend.service.map.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;


    @PostMapping("/user")
    public ResponseEntity<?> newUser(@RequestBody User newUser){

        User user = userServiceImpl.createEmployee(newUser);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/loginuser")
    public ResponseEntity<?> login (@RequestBody LoginDTO loginDTO){

        User user = userServiceImpl.loginUser(loginDTO);

        if(user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
        return userServiceImpl.findAll();
    }

    @GetMapping("/activeusers")
    List<User> getAllActiveUsers(){
        return userServiceImpl.findByStatus("активен");
    }

    @GetMapping("/applications")
    List<User> getAllApplicationd(){
        return userServiceImpl.findByStatus("заявка");
    }

    @GetMapping("/blacklist")
    List<User> getBlackList(){
        return userServiceImpl.findByStatus("заблокирован");
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable Long id){
        return userServiceImpl.findById(id);
    }

    @PutMapping("/user/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id ){

        return userServiceImpl.update(newUser,id);
    }

    @DeleteMapping("/user/{id}")
    String deleteUser(@PathVariable Long id){
        userServiceImpl.deleteById(id);
        return  "User with id "+id+" has been deleted success";
    }

}
