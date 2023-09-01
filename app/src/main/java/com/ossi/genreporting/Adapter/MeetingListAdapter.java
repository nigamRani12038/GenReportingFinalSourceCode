package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.fragment.ApprovalRejectAndApproveFragment;
import com.ossi.genreporting.fragment.SubmitMeetingFragment;
import com.ossi.genreporting.model.ShowMeetingListResponse;

import java.util.ArrayList;

public class MeetingListAdapter extends RecyclerView.Adapter<MeetingListAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<ShowMeetingListResponse> object;
    ShowMeetingListResponse exp;

    public MeetingListAdapter(ArrayList<ShowMeetingListResponse> list_task, Activity context) {
        this.object = list_task;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView emp_name_req, req_type;
        LinearLayout icon_open;

        public MyViewHolder(View view) {
            super(view);
            emp_name_req = view.findViewById(R.id.emp_name_req);
            req_type = view.findViewById(R.id.req_type);
            icon_open = view.findViewById(R.id.icon_open);

        }
    }


    @Override
    public MeetingListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meetings_view, parent, false);

        return new MeetingListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MeetingListAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String res = exp.getResponse();
        String date = exp.getMetdate();
        String emp = exp.getMetEmp();
        String department = exp.getMetDepament();
        String details = exp.getMetDetails();
        String heading = exp.getMethead();
        String mode = exp.getMetmode();
        String sno = exp.getSno();
        String time = exp.getMettime();
        String assign_by = exp.getAssign_By_Name();



        holder.emp_name_req.setText("Assign By: "+assign_by);
        holder.req_type.setText("Date "+date+" ," +time);


        holder.icon_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SubmitMeetingFragment submitMeetingFragment = new SubmitMeetingFragment();
                Bundle bundle = new Bundle();
                bundle.putString("res",res);
                bundle.putString("heading", heading);
                bundle.putString("date", date);
                bundle.putString("time", time);
                bundle.putString("emp", emp);
                bundle.putString("department", department);
                bundle.putString("details", details);
                bundle.putString("mode", mode);
                bundle.putString("sno", sno);
                bundle.putString("assign_by", assign_by);
                submitMeetingFragment.setArguments(bundle);
                openFragment1(submitMeetingFragment);
            }
        });


    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(ShowMeetingListResponse s) {
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
