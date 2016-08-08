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

package com.hacktx.android.views.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.hacktx.android.R;
import com.hacktx.android.models.Announcement;

public class AnnouncementsRecyclerView extends RecyclerView.Adapter<AnnouncementsRecyclerView.ViewHolder> {

    private static final String TAG = "AnnouncementsRecycler";
    private List<Announcement> announcementsList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView card;
        TextView date;
        TextView message;

        public ViewHolder(View v) {
            super(v);

            card = (CardView) v.findViewById(R.id.announcement_cardview);
            date = (TextView) v.findViewById(R.id.announcement_date);
            message = (TextView) v.findViewById(R.id.announcement_message);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AnnouncementsRecyclerView(List<Announcement> announcements) {
        announcementsList = announcements;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AnnouncementsRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_individual,
                parent, false);

        return new AnnouncementsRecyclerView.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Announcement message = announcementsList.get(position);
        String date = message.getFormattedTsString();

        if (date != null)
            holder.date.setText(date);

        holder.message.setText(announcementsList.get(position).getText());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (announcementsList == null) {
            announcementsList = new ArrayList<>();
        }
        return announcementsList.size();
    }
}
