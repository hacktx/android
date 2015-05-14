package com.utcs.mad.umad.models;

import android.graphics.Bitmap;

/**
 * Company
 * This is a model to hold information for all of the company sponsors/entites that are in uMAD
 */
public class Company {

    private String companyName;
    private String companyWebsite;
    private Bitmap image;
    private byte[] data;

    public Company() {
        this.companyName = "";
        this.companyWebsite = "";
        this.image = null;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public Bitmap getImage() {
        return image;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString() {
        return companyName;
    }

    public void createImage() {
        this.image = Helper.decodeBitmapFromByteArray(data, data.length, data.length);

        this.image = Bitmap.createScaledBitmap(this.image, 512, 512, true);
    }
}
