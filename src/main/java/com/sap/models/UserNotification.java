package com.sap.models;

import javax.persistence.*;

@Entity
@Table(name = "UserNotification")
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_NOTIFICATION_ID")
    private Integer id;

    private Boolean visualized;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "NOTIFICATION_ID")
    private Notification notification;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getVisualized() {
        return visualized;
    }

    public void setVisualized(Boolean visualized) {
        this.visualized = visualized;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
