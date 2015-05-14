package com.utcs.mad.umad.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.utcs.mad.umad.R;
import com.utcs.mad.umad.activities.MainActivity;
import com.utcs.mad.umad.models.Company;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Schedule Adapter
 * This adapter is to organize the event information based on time and also allows for the time
 * headers to be sticky, in order for a better UI experience
 */
public class ScheduleAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private String[] times;
    private LayoutInflater inflater;
    private ArrayList<Company> companies;

    public ScheduleAdapter(Context context, String[] times, ArrayList<Company> companies) {
        inflater = LayoutInflater.from(context);
        this.times = times;
        this.companies = companies;
    }

    private class HeaderViewHolder {
        TextView timeText;

        private HeaderViewHolder(View view) {
            timeText = (TextView) view.findViewById(R.id.textItem);
        }
    }

    private class ViewHolder {
        TextView text;
        TextView  sessionName;
        ImageView sponsorIcon;
        TextView roomInfo;

        private ViewHolder(View view) {
            text = (TextView) view.findViewById(R.id.text);
            sessionName = (TextView) view.findViewById(R.id.subtitle_text);
            sponsorIcon = (ImageView) view.findViewById(R.id.sponsor_icon);
            roomInfo = (TextView) view.findViewById(R.id.roomInfo);
        }
    }

    @Override
    public int getCount() {
        return companies.size();
    }

    @Override
    public Object getItem(int position) {
        return companies.get(position).getCompanyName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_schedule_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Update view with event info
        holder.text.setText(MainActivity.eventInfoListCache.get(position).getCompanyName());
        holder.sessionName.setText(MainActivity.eventInfoListCache.get(position).getSessionName());
        holder.roomInfo.setText(MainActivity.eventInfoListCache.get(position).getRoom());
        if (companies.get(position).getImage() == null)
            companies.get(position).createImage();
        holder.sponsorIcon.setImageBitmap(companies.get(position).getImage());

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_schedule_time_header, parent, false);
            holder = new HeaderViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        holder.timeText.setText("" + times[MainActivity.eventInfoListCache.get(position).getRegTime() - 1]);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return MainActivity.eventInfoListCache.get(position).getRegTime();
    }

}