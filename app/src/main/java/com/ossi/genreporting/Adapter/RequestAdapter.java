package com.ossi.genreporting.Adapter;

import android.app.Activity;
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

import com.ossi.genreporting.fragment.ApprovalRejectAndApproveFragment;
import com.ossi.genreporting.model.AllRequestResponse;


import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<AllRequestResponse> object;
    AllRequestResponse exp;

    public RequestAdapter(ArrayList<AllRequestResponse> list_task, Activity context) {
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
    public RequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meetings_view, parent, false);

        return new RequestAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RequestAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Emp_Name = exp.getEmpName();
        String type = exp.getType();
        String Emp_count = exp.getCount();
        String id = exp.getId();
        String purpose = exp.getPurpose();
        String fdate = exp.getFDate();
        String tdate = exp.getTDate();
        String department=exp.getDepartment();
        String reporting_manager=exp.getReportingManagers();


        holder.emp_name_req.setText(Emp_Name);
        holder.req_type.setText(type);


        holder.icon_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApprovalRejectAndApproveFragment approvalRejectAndApproveFragment = new ApprovalRejectAndApproveFragment();
                Bundle bundle = new Bundle();
                bundle.putString("Emp_Name", Emp_Name);
                bundle.putString("type", type);
                bundle.putString("Emp_count", Emp_count);
                bundle.putString("id", id);
                bundle.putString("purpose", purpose);
                bundle.putString("fdate", fdate);
                bundle.putString("tdate", tdate);
                bundle.putString("department", department);
                bundle.putString("reporting_manager", reporting_manager);
                approvalRejectAndApproveFragment.setArguments(bundle);
                openFragment1(approvalRejectAndApproveFragment);
            }
        });


    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(AllRequestResponse s) {
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