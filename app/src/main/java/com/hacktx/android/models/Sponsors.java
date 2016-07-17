package com.hacktx.android.models;

/**
 * Created by britne on 8/11/15.
 */
public class Sponsors {

    private String name;
    private String logoImage;
    private String website;
    private int level;

    public Sponsors(String name, String logoImage, String website, int level) {
        this.name = name;
        this.level = level;
        this.logoImage = logoImage;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public String getWebsite() {
        return website;
    }

    public int getLevel() {
        return level;
    }
}
