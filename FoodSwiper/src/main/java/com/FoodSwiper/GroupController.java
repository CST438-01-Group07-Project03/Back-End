package com.FoodSwiper;

import com.FoodSwiper.Entities.Groups;
import com.FoodSwiper.Entities.Users;
import com.FoodSwiper.Repositories.GroupRepository;
import com.FoodSwiper.Repositories.UsersRepository;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@RestController
class GroupController {
    private  final GroupRepository repository;
    private  final UsersRepository usersRepository;
    GroupController(GroupRepository repository, UsersRepository usersRepository){
        this.repository = repository;
        this.usersRepository = usersRepository;
    }

    // Get all groups
    @CrossOrigin
    @GetMapping("/groups")
    List<Groups> all(){
        return repository.findAll();
    }
    // Create a group
    @CrossOrigin
    @PostMapping("/groups")
    Groups newGroup(@RequestBody Groups newGroup){
        return repository.save(newGroup);
    }
    // Edit a group
    @CrossOrigin
    @PostMapping("/groups/{id}")
    Groups editGroup(@PathVariable Long id, @RequestBody Groups newGroup){
        return repository.findById(id).map(group -> {
            return repository.save(group);
        }).orElseGet(()->{
            return  repository.save(newGroup);
        });
    }
    // Add a user to a group
    @CrossOrigin
    @PostMapping("/groups/{id}/addMember/{user_id}")
    Groups addMember(@PathVariable Long id, @PathVariable Long user_id){
        return repository.findById(id).map(group->{
            Users newMember = usersRepository.findById(user_id).orElse(null);
            if(newMember == null || group.getMembers().contains(newMember))
                return group;
            group.addMember(newMember);
            return repository.save(group);
        }).orElseGet(()->{
            return null;
        });
    }
    // Delete a group
    @CrossOrigin
    @DeleteMapping("/groups/{id}")
    void deleteGroup(@PathVariable Long id){
        repository.deleteById(id);
    }
}
