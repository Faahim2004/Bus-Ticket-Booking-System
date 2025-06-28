package com.example.BusBooking.Service;

import com.example.BusBooking.Model.Users;
import com.example.BusBooking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public List<Users> userList() {
        return userRepo.findAll();
    }

    public String addUser(Users user) {
        userRepo.save(user);
        return "User added!";
    }

    public String updatePass(int id, Users user) {
        Optional<Users> userOpt = userRepo.findById(id);

        if (userOpt.isPresent()) {
            Users existUser = userOpt.get();
            existUser.setUser_name(user.getUser_name());
            existUser.setEmail(user.getEmail());
            existUser.setPassword(user.getPassword());
            userRepo.save(existUser);
            return "Updated Successfully";
        } else {
            return "ID not Found!";
        }
    }

    public void delUser(int id) throws Exception {
        if (!userRepo.existsById(id)) {
            throw new Exception("ID not Found: " + id);
        }
        userRepo.deleteById(id);
    }
}
