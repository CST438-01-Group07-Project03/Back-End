package com.FoodSwiper;

import com.FoodSwiper.Entities.Group;
import com.FoodSwiper.Repositories.GroupRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@RestController
public class GroupController {
    private final GroupRepository repository;

    GroupController(GroupRepository repository){
        this.repository = repository;
    }

    // Get all groups
    @CrossOrigin
    @GetMapping("./groups")
    List<Group> all(){
        return repository.findAll();
    }
    // Create a group
    @CrossOrigin
    @PostMapping("./groups")
    Group newGroup(@RequestBody Group newGroup){
        return repository.save(newGroup);
    }
    // Edit a group
    @CrossOrigin
    @PostMapping("./groups/{id}")
    Group editGroup(@PathVariable Long id, @RequestBody Group newGroup){
        return repository.findById(id).map(group -> {
            return repository.save(group);
        }).orElseGet(() ->{
            return repository.save(newGroup);
        });
    }
    // Delete a group
    @CrossOrigin
    @DeleteMapping("./groups/{id}")
    void deleteGroup(@PathVariable Long id){
        repository.deleteById(id);
    }
}
