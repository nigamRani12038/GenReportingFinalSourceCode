package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;

import com.ossi.genreporting.model.AbsentStatusResponse;
import com.ossi.genreporting.model.HistoryRequestResponse;

import java.util.ArrayList;

public class HistoryRequestAdapter extends RecyclerView.Adapter<HistoryRequestAdapter.MyViewHolder> implements Filterable {

    FragmentActivity context;
    private ArrayList<HistoryRequestResponse> object;
    HistoryRequestResponse exp;

    public HistoryRequestAdapter(ArrayList<HistoryRequestResponse> list_task, Activity context) {
        this.object = list_task;
        this.context = (FragmentActivity) context;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

  /*  @Override
    public Filter getFilter() {
        return null;
    }*/


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView emp_name_req, total_day_count,from,to,status_approv_reject,approved_by,leaveType,leaveStatus;

        LinearLayout icon_open;

        public MyViewHolder(View view) {
            super(view);
            emp_name_req = view.findViewById(R.id.emp_name_req);
            total_day_count = view.findViewById(R.id.total_day_count);
            from = view.findViewById(R.id.from);
            to = view.findViewById(R.id.to);
            status_approv_reject = view.findViewById(R.id.status_approv_reject);
            approved_by = view.findViewById(R.id.approved_by);
            leaveType = view.findViewById(R.id.leaveType);
            leaveStatus = view.findViewById(R.id.leaveStatus);


        }
    }


    @Override
    public HistoryRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_approval_history_view, parent, false);

        return new HistoryRequestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HistoryRequestAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Emp_Name = exp.getEmpName();
        String total_day_count = exp.getTotalDay();
        String approved_by = exp.getApprove_Name();
        String status_approved_rejected = exp.getStatus();
        String fdate = exp.getFromDate();
        String tdate = exp.getToDate();
        String res=exp.getResponse();

        holder.emp_name_req.setText(Emp_Name);
        holder.total_day_count.setText("Total Days: "+total_day_count);
        holder.approved_by.setText("By: "+approved_by);
        holder.status_approv_reject.setText("Status: "+status_approved_rejected);
        holder.from.setText("From: "+fdate);
        holder.to.setText("To: "+tdate);
        if (res.equalsIgnoreCase("Leave")){
            holder.leaveType.setVisibility(View.VISIBLE);

            String leave_Type=exp.getTypeLeave();
            String time_Status=exp.getLeaveTime();
            holder.leaveType.setText("Leave Type :  "+leave_Type);
            if(leave_Type.equalsIgnoreCase("Short Leave")){
                holder.leaveStatus.setVisibility(View.VISIBLE);
                holder.leaveStatus.setText("Time Slot:   "+time_Status);
            }else  if(leave_Type.equalsIgnoreCase("Halfday Leave")){
                holder.leaveStatus.setText("Half Day: "+time_Status);
                holder.leaveStatus.setVisibility(View.VISIBLE);
            }else {
                holder.leaveStatus.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(HistoryRequestResponse s) {
        object.add(s);
        //notifyDataSetChanged();
    }

    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    public void updateList(ArrayList<HistoryRequestResponse> list) {
        this.object = list;
        notifyDataSetChanged();
    }
}