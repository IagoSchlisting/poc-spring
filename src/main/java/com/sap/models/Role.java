package com.sap.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ROLE_ID")
    private Integer id;

    private String role;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(
//            name = "User_Roles",
//            joinColumns = { @JoinColumn(name = "ROLE_ID") },
//            inverseJoinColumns = { @JoinColumn(name = "USER_ID") }
//    )
//    private List<User> users;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
