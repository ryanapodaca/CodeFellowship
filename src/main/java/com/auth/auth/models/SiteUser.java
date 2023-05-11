package com.auth.auth.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
public class SiteUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    String username;
    private String password;
    String firstName;
    String lastName;
    String dateOfBirth;
    String bio;
    LocalDate dateCreated;
    @OneToMany(mappedBy = "siteUser")
    List<Post> posts;

    @ManyToMany
    @JoinTable (
        name = "followerToFollowees",
        joinColumns = {@JoinColumn(name = "userWhoIsFollowing")},
        inverseJoinColumns = {@JoinColumn(name = "FollowedUser")}
    )
    Set<SiteUser> usersIFollow = new HashSet<>();

    @ManyToMany(mappedBy = "usersIFollow")
    Set<SiteUser> usersWhoFollowMe = new HashSet<>();



    public Set<SiteUser> getUsersIFollow() {
        return usersIFollow;
    }

    public Set<SiteUser> getUsersWhoFollowMe() {
        return usersWhoFollowMe;
    }

    public SiteUser() {}

    public SiteUser(String username, String password, String firstName, String lastName, String dateOfBirth, String bio, LocalDate dateCreated) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.dateCreated = dateCreated;
    }

    public ArrayList<List<Post>> getUsersIFollowPosts(){
        ArrayList<List<Post>> list = new ArrayList<>();
        for(SiteUser followee : usersIFollow){
            list.add(followee.posts);
        }
        return list;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        username = userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


