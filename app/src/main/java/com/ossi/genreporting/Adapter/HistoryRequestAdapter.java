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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;

import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.fragment.ApprovedRequestHistoryFragment;
import com.ossi.genreporting.fragment.PayrollAttendanceApprovalFragment;
import com.ossi.genreporting.model.AbsentStatusResponse;
import com.ossi.genreporting.model.AdjustResponse;
import com.ossi.genreporting.model.HistoryRequestResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryRequestAdapter extends RecyclerView.Adapter<HistoryRequestAdapter.MyViewHolder> implements Filterable {

    FragmentActivity context;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<HistoryRequestResponse> object;
    HistoryRequestResponse exp;
    String user_id;
    private SharedPreferences pref;
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

        ImageView deleteRequest;

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
            deleteRequest=view.findViewById(R.id.deleteRequest);


        }
    }


    @Override
    public HistoryRequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.request_approval_history_view, parent, false);
        pref = context.getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id =  pref.getString("user_id", null);

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
        String leave_id = exp.getId();
        String res=exp.getResponse();

        holder.emp_name_req.setText(Emp_Name);
        holder.total_day_count.setText("Total Days: "+total_day_count);
        holder.approved_by.setText(" By: "+approved_by);
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
        if(status_approved_rejected.equalsIgnoreCase("Approved")){
            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if(fdate.compareTo(currentDate) == 0||currentDate.compareTo(fdate) < 0) {
                holder.deleteRequest.setVisibility(View.VISIBLE);
            }
        }else {
            holder.deleteRequest.setVisibility(View.GONE);
        }
        holder.deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to Cancel this Request");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        cancel_req_after_approve(user_id,leave_id,total_day_count,res);
                        dialog.dismiss();


                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

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

    private void cancel_req_after_approve(String userId,String leaveId,String totalDays,String leaveType) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AdjustResponse>> call1 = apiInterface.cancel_req_after_approve(userId,leaveId,totalDays,leaveType);
        call1.enqueue(new Callback<List<AdjustResponse>>() {
            @Override
            public void onResponse(Call<List<AdjustResponse>> call, Response<List<AdjustResponse>> response) {
                List<AdjustResponse> adjustResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (adjustResponse != null && adjustResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                    for (int i = 0; i < adjustResponse.size(); i++) {
                       String res = adjustResponse.get(i).getResponse();
                        if(res.equalsIgnoreCase("success")){
                            Toast.makeText(context, "Request Cancel Successfully", Toast.LENGTH_SHORT).show();
                            ApprovedRequestHistoryFragment approvedRequestHistoryFragment=new ApprovedRequestHistoryFragment();
                            openFragment1(approvedRequestHistoryFragment);


                        }else {
                            Toast.makeText(context, ""+res, Toast.LENGTH_SHORT).show();
                        }


                    }




                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<AdjustResponse>> call, Throwable t) {
                Toast.makeText(context, "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    public void updateList(ArrayList<HistoryRequestResponse> list) {
        this.object = list;
        notifyDataSetChanged();
    }
}