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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import com.hacktx.android.R;
import com.hacktx.android.models.Sponsors;

public class SponsorsRecyclerView extends RecyclerView.Adapter<SponsorsRecyclerView.ViewHolder>  {

    private static final String TAG = "SponsorsRecycler";
    private List<Sponsors> sponsorsList;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView logo;

        public ViewHolder(View v) {
            super(v);

            logo = (ImageView) v.findViewById(R.id.sponsor_logo);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SponsorsRecyclerView(List<Sponsors> announcements, Context context) {
        sponsorsList = announcements;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SponsorsRecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sponsor_individual,
                parent, false);
        return new SponsorsRecyclerView.ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Picasso.with(context).load(sponsorsList.get(position).getLogoImage())
                .into(holder.logo);
        holder.logo.setContentDescription(sponsorsList.get(position).getName());
        holder.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(sponsorsList.get(position).getWebsite()));
                context.startActivity(browserIntent);
            }
        });

        ViewGroup.LayoutParams layoutParams = holder.logo.getLayoutParams();
        layoutParams.height = 300;
        holder.logo.setLayoutParams(layoutParams);



    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (sponsorsList == null) {
            sponsorsList = new ArrayList<>();
        }
        return sponsorsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return sponsorsList.get(position).getLevel();
    }
}
