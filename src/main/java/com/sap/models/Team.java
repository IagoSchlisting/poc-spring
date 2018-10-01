package com.sap.models;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "TEAM")
@Repository
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TEAM_ID")
    private Integer id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "team")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private List<User> users;

    @OneToMany(mappedBy = "team")
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
    private List<Period> periods;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

}
