package com.sprint.mission.discodeit.entity;

public class User extends DefaultEntity {

    private String name;
    private String email;
    private String password;

    public User(String name, String password) {
        super();
        this.name = name;
        this.password = password;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email + "]";
    }
}
