package com.ossi.genreporting.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.fragment.PayrollAttendanceApprovalFragment;
import com.ossi.genreporting.model.AdjustResponse;
import com.ossi.genreporting.model.PayrollAttendanceShowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceAprovalShowAdapter extends RecyclerView.Adapter<AttendanceAprovalShowAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<PayrollAttendanceShowResponse> object;
    PayrollAttendanceShowResponse exp;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private SharedPreferences pref;
    private String user_id,edit;
    private String res;

    public AttendanceAprovalShowAdapter(ArrayList<PayrollAttendanceShowResponse> list_task, Activity context) {
        this.object = list_task;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_att,total_days_att,total_present_day_att,total_leave_att,total_absent_day_att,total_weekoff_day_att,total_adjust_day_att;
        EditText edit_days;
        Button save_adjust_day;


        public MyViewHolder(View view) {
            super(view);
            name_att = view.findViewById(R.id.name_att);
            total_days_att = view.findViewById(R.id.total_days_att);
            total_present_day_att = view.findViewById(R.id.total_present_day_att);
            total_leave_att = view.findViewById(R.id.total_leave_att);
            total_absent_day_att = view.findViewById(R.id.total_absent_day_att);
            total_weekoff_day_att = view.findViewById(R.id.total_weekoff_day_att);
            total_adjust_day_att = view.findViewById(R.id.total_adjust_day_att);
            edit_days = view.findViewById(R.id.edit_days);
            save_adjust_day = view.findViewById(R.id.save_adjust_day);



        }
    }


    @Override
    public AttendanceAprovalShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_approval_show_data, parent, false);

        pref = context.getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id =  pref.getString("user_id", null);


        return new AttendanceAprovalShowAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AttendanceAprovalShowAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String unique_id=exp.getId();
        String Emp_Name = exp.getEmpName();
        String Total_day = exp.getTotalDay();
        String Total_absent = exp.getTotalAbsent();
        String Adjust_day = exp.getAdjustDay();
        String Total_leave = exp.getTotalLeave();
        String Total_present = exp.getTotalPresent();
        String weekOff = exp.getWeekOff();



        holder.name_att.setText(Emp_Name);
        holder.total_days_att.setText(Total_day);
        holder.total_absent_day_att.setText(Total_absent);
        holder.total_adjust_day_att.setText(Adjust_day);
        holder.total_leave_att.setText(Total_leave);
        holder.total_present_day_att.setText(Total_present);
        holder.total_weekoff_day_att.setText(weekOff);

      holder.save_adjust_day.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
               edit=holder.edit_days.getText().toString();
              if (edit.equalsIgnoreCase("")){
                  Toast.makeText(context, "Please Enter Udjust Day", Toast.LENGTH_SHORT).show();
              }else {
                  if (isNetworkAvailable()) {
                      adjust_day(user_id,unique_id,edit);
                  }else {
                      Toast.makeText(context, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                  }
              }

          }
      });
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(PayrollAttendanceShowResponse s) {
        object.add(s);
        //notifyDataSetChanged();
    }
    private void adjust_day(String user_id,String unique_id,String edit) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AdjustResponse>> call1 = apiInterface.adjust_day_edit(user_id,unique_id,edit);
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
                        res = adjustResponse.get(i).getResponse();
                        if(res.equalsIgnoreCase("success")){
                            PayrollAttendanceApprovalFragment payrollAttendanceApprovalFragment=new PayrollAttendanceApprovalFragment();
                            openFragment1(payrollAttendanceApprovalFragment);


                        }
                        else if (res.equalsIgnoreCase("Adjust day should not be more than Absent Day")) {
                            Toast.makeText(context, "Adjust day should not be more than Absent Day", Toast.LENGTH_SHORT).show();
                        }
                        else if(res.equalsIgnoreCase("Invalid Id")){
                            Toast.makeText(context, "Invalid Id", Toast.LENGTH_SHORT).show();


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



    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }
    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) context.getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}