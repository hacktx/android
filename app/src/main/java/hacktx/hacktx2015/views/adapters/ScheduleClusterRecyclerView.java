package hacktx.hacktx2015.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hacktx.hacktx2015.R;
import hacktx.hacktx2015.models.ScheduleCluster;
import hacktx.hacktx2015.models.ScheduleEvent;

public class ScheduleClusterRecyclerView extends RecyclerView.Adapter<ScheduleClusterRecyclerView.ViewHolder> {

    ArrayList<ScheduleCluster> scheduleClusterList;
    private ScheduleItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView groupTime;
        LinearLayout groupEvents;

        public ViewHolder(View itemView) {
            super(itemView);
            groupTime = (TextView) itemView.findViewById(R.id.eventGroupTitle);
            groupEvents = (LinearLayout) itemView.findViewById(R.id.eventHolderLayout);
        }
    }

    public interface ScheduleItemClickListener {
        void onItemClick(View v, ScheduleEvent e);
    }

    public ScheduleClusterRecyclerView(ArrayList<ScheduleCluster> scheduleClusterList,
                                       ScheduleItemClickListener listener) {
        this.scheduleClusterList = scheduleClusterList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_groupview, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.groupTime.setText(scheduleClusterList.get(position).getName());
        createEventViews(holder.groupEvents, scheduleClusterList.get(position).getEventsList());
    }

    @Override
    public int getItemCount() {
        return scheduleClusterList.size();
    }

    private void createEventViews(LinearLayout groupEvents, ArrayList<ScheduleEvent> eventList) {
        groupEvents.removeAllViews();
        for(int child = 0; child < eventList.size(); child++) {
            final ScheduleEvent curEvent = eventList.get(child);
            View childView = LayoutInflater.from(groupEvents.getContext()).inflate(R.layout.schedule_eventview, groupEvents, false);

            int eventIconId;
            switch(curEvent.getType()) {
                case TALK: eventIconId = R.drawable.ic_event_talk; break;
                case EDUCATION: eventIconId = R.drawable.ic_event_education; break;
                case BUS: eventIconId = R.drawable.ic_event_bus; break;
                case FOOD: eventIconId = R.drawable.ic_event_food; break;
                case DEV: eventIconId = R.drawable.ic_event_dev; break;
                default: eventIconId = R.drawable.ic_event_default; break;
            }

            ImageView eventIcon = (ImageView) childView.findViewById(R.id.eventIcon);
            eventIcon.setImageResource(eventIconId);
            TextView eventName = (TextView) childView.findViewById(R.id.eventName);
            eventName.setText(curEvent.getName());
            TextView eventDetails = (TextView) childView.findViewById(R.id.eventDetails);
            eventDetails.setText(curEvent.getEventDetails());

            childView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, curEvent);
                }
            });

            groupEvents.addView(childView);
        }
    }
}
