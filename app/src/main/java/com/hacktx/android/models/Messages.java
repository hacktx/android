/*
 * Copyright 2016 HackTX.
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

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Messages {

    private String text;
    private String ts;

    public Messages(String text, String ts) {
        this.text = text;
        this.ts = ts;
    }

    public String getText() {
        return text;
    }

    public String getTs() {
        return ts;
    }

    public Date getTsDate() {
        SimpleDateFormat formatFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

        try {
            return formatFrom.parse(getTs());
        }
        catch (ParseException e) {
            Log.d("MessagesModel", e.getMessage());
            return null;
        }
    }

    public String getFormattedTsString() {
        //format date object to new date
        SimpleDateFormat formatTo = new SimpleDateFormat("MMM dd, hh:mma", Locale.US);
        return formatTo.format(getTsDate());
    }

    public static Comparator<Messages> MessagesComparator
            = new Comparator<Messages>() {

        public int compare(Messages message1, Messages message2) {
            return message2.getTsDate().compareTo(message1.getTsDate());
        }

    };

}
