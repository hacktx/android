package com.hacktx.android.models;

/**
 * Created by Drew on 6/27/15.
 */
public class ScheduleSpeaker {
    private int id;
    private String name;
    private String organization;
    private String description;
    private String imageUrl;

    public ScheduleSpeaker(int id, String name, String organization, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.organization = organization;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
