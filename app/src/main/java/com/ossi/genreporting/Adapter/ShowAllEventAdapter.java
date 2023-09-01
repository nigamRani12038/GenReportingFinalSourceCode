package com.ossi.genreporting.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.fragment.AddEventFragment;
import com.ossi.genreporting.model.AllEventsShow;
import com.ossi.genreporting.model.AllEventsShow;

import java.util.ArrayList;

public class ShowAllEventAdapter extends RecyclerView.Adapter<ShowAllEventAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<AllEventsShow> object;
    AllEventsShow exp;
    private SharedPreferences pref;
    String login_type;

    public ShowAllEventAdapter(ArrayList<AllEventsShow> list_event, Activity context) {
        this.object = list_event;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView event, wish,name_evnt;
        ImageView add_event;

        public MyViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.event);
            wish = view.findViewById(R.id.wish);
            add_event = view.findViewById(R.id.add_event);
            name_evnt=view.findViewById(R.id.name_evnt);

        }
    }


    @Override
    public ShowAllEventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_event, parent, false);
        pref = context.getSharedPreferences("my_pref", MODE_PRIVATE);

        login_type = pref.getString("login_type", " ");


        return new ShowAllEventAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShowAllEventAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String event_name = exp.getEventName();
        String event_date = exp.getEventDate();
        String event_description = exp.getEventDescription();
        String event_id = exp.getId();
        String event_s_no = exp.getSno();
        String event_db_time = exp.getDBTime();

        holder.name_evnt.setText(event_name);
        holder.event.setText(event_description);
        holder.wish.setText(event_date );

       /* if (event_name.equalsIgnoreCase("DOB")) {
            holder.event.setText("It is " + event_name + " Birthday Today");
            holder.wish.setText("write best wishes on his wall");
            //It is Utkarsh's Birthday Today
        } else if (Emp_Event.equalsIgnoreCase("Married")) {
            holder.event.setText("It is " + event_name + " Work Aniversary Today");
            holder.wish.setText("write best wishes on his wall");
        } else if (Emp_Event.equalsIgnoreCase("HoliDay")) {

            String[] dateParts = event_name.split(",");
            String eventname = dateParts[0];
            String date = dateParts[1];
            holder.event.setText("It is " + eventname + " You have a Holiday on the " + date);
            holder.wish.setText("Today your Enjoy day");
        }
*/
       /* if (login_type.equalsIgnoreCase("1")) {
            holder.add_event.setVisibility(View.VISIBLE);

        } else if (login_type.equalsIgnoreCase("2")) {
            holder.add_event.setVisibility(View.VISIBLE);
        } else {
            holder.add_event.setVisibility(View.GONE);
        }
*/
        holder.add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventFragment addEventFragment = new AddEventFragment();
                openFragment1(addEventFragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(AllEventsShow s) {
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
