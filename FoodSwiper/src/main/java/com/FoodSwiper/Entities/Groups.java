package com.FoodSwiper.Entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Peter Gloag
 * @since 4/22/2026
 */
@Entity
public class Groups {
    @Id
    @GeneratedValue
    private long id;
    private String name;

    @OneToMany
    private List<Users> members;
    public Groups(String name){
        this.name = name;
        this.members = new ArrayList<>();
    }
    public Groups(){
        this("");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Users> getMembers(){
        return this.members;
    }

    public void setMembers(List<Users> members){
        this.members = members;
    }

    public void addMember(Users new_member){
        this.members.add(new_member);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Groups groups = (Groups) o;
        return id == groups.id && Objects.equals(name, groups.name) && Objects.equals(members, groups.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, members);
    }

    @Override
    public String toString() {
        return "Groups{" +
                "name='" + name + '\'' +
                "members=" + members +
                ", id=" + id +
                '}';
    }
}
