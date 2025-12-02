package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {

    private String name;
    private String email;
    private transient String password;

    public User(String name, String email, String password) {
        super();
        this.name = name;
        this.email = email;
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
    // DTO를 매개변수로 받아 사용하려 했으나
    // DTO에 의존적이게 되는것으로 판단하여 각각 수정하기 위한 것들만 따로 받음
    public void update(String name, String email, String password) {
        if (name != null) this.name = name;
        if (email != null) this.email = email;
        if (password != null) this.password = password;

        super.setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", email=" + email + "password=" + password + "]";
    }
}
