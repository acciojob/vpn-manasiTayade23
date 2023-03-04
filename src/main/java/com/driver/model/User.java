package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String originalIp;
    private String maskedIp;
    private boolean Connected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOriginalIp() {
        return originalIp;
    }

    public void setOriginalIp(String originalIp) {
        this.originalIp = originalIp;
    }

    public String getMaskedIp() {
        return maskedIp;
    }

    public void setMaskedIp(String maskedIp) {
        this.maskedIp = maskedIp;
    }

    public boolean getConnected() {
        return Connected;
    }

    public void setConnected(boolean connected) {
        Connected = connected;
    }



//    public User(int id, String username, String password, String originalIp, String maskedIp, boolean isConnected) {
//        this.id = id;
//        this.username = username;
//        this.password = password;
//        this.originalIp = originalIp;
//        this.maskedIp = maskedIp;
//        this.isConnected = isConnected;
//    }
    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Country OriginalCountry;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Connection> connectionList=new ArrayList<>();
    
    @ManyToMany
    @JoinColumn
    private List<ServiceProvider> Users =new ArrayList<>();

    public Country getOriginalCountry() {
        return OriginalCountry;
    }

    public void setOriginalCountry(Country originalCountry) {
        this.OriginalCountry = originalCountry;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public List<ServiceProvider> getUsers() {
        return Users;
    }

    public void setUsers(List<ServiceProvider> users) {
        this.Users = users;
    }
}
