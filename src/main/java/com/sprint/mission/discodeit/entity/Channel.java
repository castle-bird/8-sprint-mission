package com.sprint.mission.discodeit.entity;

import java.io.Serial;

public class Channel extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;

    public Channel(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    // Getter
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // Setter
    public void update(String name, String description) {
        if (name != null) this.name = name;
        if (description != null) this.description = description;

        super.setUpdatedAt(System.currentTimeMillis());
    }


    @Override
    public String toString() {
        return "Channel [name=" + name + ", description=" + description + "]";
    }
}
