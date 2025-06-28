package com.example.BusBooking.Controller;


import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/getall")
    public List<Users> getAllUsers(@RequestBody Users users){
        return userService.userList();
    }


    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody Users user){
        return new ResponseEntity<>(userService.addUser(user), HttpStatus.ACCEPTED);
    }

    @PatchMapping("updatePass/{id}")
    public String updateUser(@PathVariable int id,@RequestBody Users user){
        return userService.updatePass(id,user);
    }

    @DeleteMapping("deleteUser/{id}")
    public ResponseEntity<String> delUser(@PathVariable int id) throws Exception {
        userService.delUser(id);
        return new ResponseEntity<>("Item Deleted", HttpStatus.OK);
    }

}

