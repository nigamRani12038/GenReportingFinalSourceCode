package com.ossi.genreporting.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.fragment.AddEventFragment;
import com.ossi.genreporting.fragment.AddShowEventFragment;
import com.ossi.genreporting.model.AllEventsShow;
import com.ossi.genreporting.model.AllEventsShow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowAllEventAdapter extends RecyclerView.Adapter<ShowAllEventAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<AllEventsShow> object;
    AllEventsShow exp;
    private SharedPreferences pref;
    String login_type;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;



    public ShowAllEventAdapter(ArrayList<AllEventsShow> list_event, Activity context) {
        this.object = list_event;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView event, wish, name_evnt;
        ImageView delete, edit;

        public MyViewHolder(View view) {
            super(view);
            event = view.findViewById(R.id.event);
            wish = view.findViewById(R.id.wish);
            delete = view.findViewById(R.id.delete);
            name_evnt = view.findViewById(R.id.name_evnt);
            edit = view.findViewById(R.id.edit);

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
        String organiser = exp.getOrganiser();
        String venue = exp.getVenue();
        String location = exp.getLocation();
        String event_id = exp.getId();


        holder.delete.setVisibility(View.VISIBLE);
        holder.edit.setVisibility(View.VISIBLE);

        holder.name_evnt.setText("Event Name: " + event_name);
        holder.event.setText("Event Details: " + event_description + "\n" + "Venue: " + venue + "\n" + "Location: " + location);
        holder.wish.setText(event_date + "  Organiser: " + organiser);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEventFragment addEventFragment = new AddEventFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Edit_event","Edit_event");
                bundle.putString("event_name",event_name);
                bundle.putString("event_date", event_date);
                bundle.putString("event_description", event_description);
                bundle.putString("organiser",organiser);
                bundle.putString("venue",venue);
                bundle.putString("location",location);
                bundle.putString("event_id",event_id);
                addEventFragment.setArguments(bundle);
                openFragment1(addEventFragment);
            }
        });


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.isNetworkAvailable(context)) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are You sure You want to Delete This Event");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    deleteEvent(event_id);
                                    dialog.cancel();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                } else {
                    Toast.makeText(context, "Please check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }



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

    private void deleteEvent(String event_id) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AllEventsShow>> call1 = apiInterface.delete_events_lists(event_id);
        call1.enqueue(new Callback<List<AllEventsShow>>() {
            @Override
            public void onResponse(Call<List<AllEventsShow>> call, Response<List<AllEventsShow>> response) {
                List<AllEventsShow> my_task_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_task_res != null && my_task_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < my_task_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                       String res_success = my_task_res.get(i).getResponse();
                        if (res_success.equalsIgnoreCase("success")) {
                            Toast.makeText(context, "Delete Event successfully", Toast.LENGTH_SHORT).show();
                            AddShowEventFragment addShowEventFragment = new AddShowEventFragment();
                            openFragment1(addShowEventFragment);
                        }else if(res_success.equalsIgnoreCase("Fail")){
                            Toast.makeText(context, "Delete Failed Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<AllEventsShow>> call, Throwable t) {
                Toast.makeText(context, "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}
