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
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;

import com.ossi.genreporting.model.PayrollSalaryShowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaryApprovalShowAdapter extends RecyclerView.Adapter<SalaryApprovalShowAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<PayrollSalaryShowResponse> object;
    PayrollSalaryShowResponse exp;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private SharedPreferences pref;
    private String user_id,id,edit;

    public SalaryApprovalShowAdapter(ArrayList<PayrollSalaryShowResponse> list_task, Activity context) {
        this.object = list_task;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name_att,gross_total,total_deducted,net_salary,total_ctc,voucher;



        public MyViewHolder(View view) {
            super(view);
            name_att = view.findViewById(R.id.name_att);
            gross_total = view.findViewById(R.id.gross_total);
            total_deducted = view.findViewById(R.id.total_deducted);
            net_salary = view.findViewById(R.id.net_salary);
            total_ctc = view.findViewById(R.id.total_ctc);
            voucher = view.findViewById(R.id.voucher);



        }
    }


    @Override
    public SalaryApprovalShowAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.salary_approval_show_data, parent, false);

        pref = context.getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id =  pref.getString("user_id", null);


        return new SalaryApprovalShowAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SalaryApprovalShowAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Emp_Name = exp.getEmpName();
        String Total_gross = exp.getGrossTotal();
        String Total_ctc = exp.getTotalCTC();
        String net_salary = exp.getNetSalary();
        String voucher = exp.getVoucher();
        String Total_deduction = exp.getTotalDeducted();
        String response = exp.getResponse();







        holder.name_att.setText(Emp_Name);
        holder.gross_total.setText(Total_gross);
        holder.total_ctc.setText(Total_ctc);
        holder.net_salary.setText(net_salary);
        holder.voucher.setText(voucher);
        holder.total_deducted.setText(Total_deduction);
        //holder.total_weekoff_day_att.setText(weekOff);


    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(PayrollSalaryShowResponse s) {
        object.add(s);
        //notifyDataSetChanged();
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