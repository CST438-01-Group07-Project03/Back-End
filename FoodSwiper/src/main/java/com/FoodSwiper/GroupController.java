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
    // Get a group by id
    @CrossOrigin
    @GetMapping("/groups/{id}")
    Groups getGroup(@PathVariable Long id){
        return repository.findById(id).orElse(null);
    }
    // Create a group
    @CrossOrigin
    @PostMapping("/groups")
    Groups newGroup(@RequestBody Groups newGroup){
        return repository.save(newGroup);
    }
    // Edit a group
    @CrossOrigin
    @PutMapping("/groups/{id}")
    Groups editGroup(@PathVariable Long id, @RequestBody Groups newGroup){
        return repository.findById(id).map(group -> {
            return repository.save(group);
        }).orElseGet(()->{
            return  repository.save(newGroup);
        });
    }
    // Add a user to a group
    @CrossOrigin
    @PutMapping("/groups/{id}/addMember/{user_id}")
    Groups addMember(@PathVariable Long id, @PathVariable Long user_id){
        return repository.findById(id).map(group->{
            Users newMember = usersRepository.findById(user_id).orElse(null);
            if(newMember == null || group.getMembers().contains(newMember))
                return group;
            group.addMember(newMember);

            usersRepository.findById(user_id).map(user ->{
                user.addGroup(id);
                return  usersRepository.save(user);
            });

            return repository.save(group);
        }).orElseGet(()->{
            return null;
        });
    }
    // Remove a user from a group
    @CrossOrigin
    @PutMapping("/groups/{id}/removeMember/{user_id}")
    void removeMember(@PathVariable Long id, @PathVariable Long user_id){
        Users to_remove = usersRepository.findById(user_id).orElse(null);
        repository.findById(id).map(group->{
            group.removeMember(to_remove);
            usersRepository.findById(user_id).map(user ->{
                user.removeGroup(id);
                return usersRepository.save(user);
            });
            return repository.save(group);
        });
    }
    // Delete a group
    @CrossOrigin
    @DeleteMapping("/groups/{id}")
    void deleteGroup(@PathVariable Long id){
        repository.deleteById(id);
    }
}
