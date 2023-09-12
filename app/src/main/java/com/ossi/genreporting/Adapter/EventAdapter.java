package com.ossi.genreporting.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.fragment.AddEventFragment;
import com.ossi.genreporting.fragment.TaskSubmitFragment;
import com.ossi.genreporting.model.EventResponse;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<EventResponse> object;
    EventResponse exp;
    private SharedPreferences pref;
    String login_type;

    public EventAdapter(ArrayList<EventResponse> list_event, Activity context) {
        this.object = list_event;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView event, wish;
        ImageView delete,edit;
        LinearLayout lin_event;

        public MyViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.event);
            wish = view.findViewById(R.id.wish);
            delete = view.findViewById(R.id.delete);
            lin_event=view.findViewById(R.id.lin_event);
            edit=view.findViewById(R.id.edit);

        }
    }


    @Override
    public EventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        pref = context.getSharedPreferences("my_pref", MODE_PRIVATE);

        login_type = pref.getString("login_type", " ");


        return new EventAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String eventType = exp.getEventType();
        String event_name = exp.getEmpName();
        String description = exp.getEventDescription();
        String venue = exp.getVenue();
        String location = exp.getLocation();
        String organiser = exp.getOrganiser();
        String res = exp.getResponse();

        if (res.equalsIgnoreCase("success")){
            if (eventType.equalsIgnoreCase("DOB")) {
                holder.event.setText("It is " + event_name + " Birthday Today");
                holder.wish.setText("write best wishes on his wall");
                //It is Utkarsh's Birthday Today
            } else if (eventType.equalsIgnoreCase("Married")) {
                holder.event.setText("It is " + event_name + " Work Aniversary Today");
                holder.wish.setText("write best wishes on his wall");
            } else if (eventType.equalsIgnoreCase("HoliDay")) {

           /* String[] dateParts = event_name.split(",");
            String eventname = dateParts[0];
            String date = dateParts[1];*/
                holder.event.setText("You have a Holiday on the " + description);
                holder.wish.setText("Today your Enjoy day");
            } else {
                holder.event.setText("Event Name: " + eventType + "\n" + "Event Details: " + description + "\n" + "Venue: " + venue + "\n" + "Location: " + location);
                holder.wish.setText("  Organiser: " + organiser);


            }


        holder.edit.setVisibility(View.GONE);
        holder.delete.setVisibility(View.GONE);
    }else if(res.equalsIgnoreCase("Fail")){
            holder.lin_event.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(EventResponse s) {
        object.add(s);
        //notifyDataSetChanged();
    }
    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}
