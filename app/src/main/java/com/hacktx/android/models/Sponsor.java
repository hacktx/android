/*
 * Copyright 2017 HackTX.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hacktx.android.models;

public class Sponsor {

    private String name;
    private String logoImage;
    private String website;
    private int level;

    public Sponsor(String name, String logoImage, String website, int level) {
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
