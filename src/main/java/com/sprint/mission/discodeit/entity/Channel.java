package com.sprint.mission.discodeit.entity;

public class Channel extends DefaultEntity {

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
    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Channel [name=" + name + ", description=" + description + "]";
    }
}
